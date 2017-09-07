package com.socool.soft.vo;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({@org.apache.ibatis.plugin.Signature(type=org.apache.ibatis.executor.Executor.class, method="query", args={MappedStatement.class, Object.class, org.apache.ibatis.session.RowBounds.class, org.apache.ibatis.session.ResultHandler.class})})
public class PaginationInterceptor
  implements Interceptor
{
//  private static final Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);
//  private Dialect dialect;
//
//  public PaginationInterceptor()
//  {
//    this.dialect = null;
//  }

  public Object intercept(Invocation invocation) throws Throwable {
    MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
    Object parameter = invocation.getArgs()[1];
    BoundSql boundSql = mappedStatement.getBoundSql(parameter);

    if ((boundSql == null) || (boundSql.getSql() == null) || ("".equals(boundSql.getSql()))) {
      return null;
    }

    String originalSql = boundSql.getSql().trim();
    Object parameterObject = boundSql.getParameterObject();

    Page page = getPage(parameterObject);

    if ((page != null) && (page.isPagination()))
    {
      if (page.getTotalRows() == 0L) {
        StringBuffer countSql = new StringBuffer(originalSql.length() + 100);
        countSql.append("select count(1) from (").append(originalSql).append(") t");
        Connection connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
        PreparedStatement countStmt = connection.prepareStatement(countSql.toString());
        BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql.toString(), boundSql.getParameterMappings(), parameterObject);
        for (ParameterMapping mapping : countBS.getParameterMappings()) {
          String prop = mapping.getProperty();
          if (boundSql.hasAdditionalParameter(prop)) {
            countBS.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
          }
        }
        setParameters(countStmt, mappedStatement, countBS, parameterObject);
        ResultSet rs = countStmt.executeQuery();
        if (rs.next()) {
          page.setTotalRows(rs.getLong(1));
        }
        rs.close();
        countStmt.close();
        connection.close();
      }

      String pagesql = this.getPageSql(originalSql, page.getPageStartRow(), page.getPageSize());
      BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pagesql, boundSql.getParameterMappings(), boundSql.getParameterObject());

      for (ParameterMapping mapping : boundSql.getParameterMappings()) {
        String prop = mapping.getProperty();
        if (boundSql.hasAdditionalParameter(prop)) {
          newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
        }
      }
//      logger.debug("生成分页SQL :{} ", newBoundSql.getSql());
      MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
      invocation.getArgs()[0] = newMs;
    }

    return invocation.proceed();  
  }

  private Page getPage(Object parameterObject)
  {
    Page page = PageContext.getPage();
    if (page != null) {
      PageContext.removePage();
      return page;
    }

    if (parameterObject != null) {
      page = (Page)isPage(parameterObject);
    }
    return page;
  }

  public Object plugin(Object target)
  {
    return Plugin.wrap(target, this);
  }

  public void setProperties(Properties properties)
  {
//    String dialectClass = properties.getProperty("dialect");
//    if (StringUtils.isBlank(dialectClass)) {
//      this.dialect = new OracleDialect();
//      logger.info("没有配置 myBatis 的方言，将使用默认值：{}", OracleDialect.class);
//    } else {
//      try {
//        this.dialect = ((Dialect)Class.forName(dialectClass).newInstance());
//        logger.info("myBatis 使用方言：{}", dialectClass);
//      } catch (Exception e) {
//        throw new RuntimeException(new StringBuilder().append("cannot create dialect instance by dialectClass: ").append(dialectClass).toString(), e);
//      }
//    }
  }

  private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject)
    throws SQLException
  {
    ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
    List parameterMappings = boundSql.getParameterMappings();
    if (parameterMappings != null) {
      Configuration configuration = mappedStatement.getConfiguration();
      TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
      MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
      for (int i = 0; i < parameterMappings.size(); i++) {
        ParameterMapping parameterMapping = (ParameterMapping)parameterMappings.get(i);
        if (parameterMapping.getMode() != ParameterMode.OUT)
        {
          String propertyName = parameterMapping.getProperty();
          PropertyTokenizer prop = new PropertyTokenizer(propertyName);
          Object value;
          if (parameterObject == null) {
            value = null;
          }
          else
          {
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
              value = parameterObject;
            }
            else
            {
              if (boundSql.hasAdditionalParameter(propertyName)) {
                value = boundSql.getAdditionalParameter(propertyName);
              } else if ((propertyName.startsWith("__frch_")) && (boundSql.hasAdditionalParameter(prop.getName())))
              {
                value = boundSql.getAdditionalParameter(prop.getName());
                if (value != null)
                  value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
              }
              else
              {
                value = metaObject == null ? null : metaObject.getValue(propertyName);
              }
            }
          }
          TypeHandler typeHandler = parameterMapping.getTypeHandler();
          if (typeHandler == null) {
            throw new ExecutorException(new StringBuilder().append("There was no TypeHandler found for parameter ").append(propertyName).append(" of statement ").append(mappedStatement.getId()).toString());
          }

          typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
        }
      }
    }
  }
  public String getPageSql(String sql, long offset, long pageSize)
  {
    StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
    pagingSelect.append("").append(sql).append(" limit  ").append(offset).append(" , ").append(pageSize);

    return pagingSelect.toString();
  }
  private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource)
  {
    MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

    builder.resource(ms.getResource());
    builder.fetchSize(ms.getFetchSize());
    builder.statementType(ms.getStatementType());
    builder.keyGenerator(ms.getKeyGenerator());
    builder.keyProperty(convertArrayToString(ms.getKeyProperties()));
    builder.timeout(ms.getTimeout());
    builder.parameterMap(ms.getParameterMap());
    builder.resultMaps(ms.getResultMaps());
    builder.useCache(ms.isUseCache());
    builder.cache(ms.getCache());
    MappedStatement newMs = builder.build();
    return newMs;
  }

  private String convertArrayToString(String[] in)
  {
    String returnStr = null;
    if ((in == null) || (in.length == 0)) {
      returnStr = null;
    } else {
      StringBuilder builder = new StringBuilder();
      for (String str : in) {
        builder.append(str).append(",");
      }

      if ((builder.length() > 0) && (builder.indexOf(",") != -1))
      {
        returnStr = builder.substring(0, builder.lastIndexOf(","));
      }
    }
    return returnStr;
  }

  private Object isPage(Object obj)
  {
    if ((obj instanceof Map)) {
      Map map = (Map)obj;
      Iterator iter = map.values().iterator();
      while (iter.hasNext()) {
        Object o = iter.next();
        if ((o instanceof Page))
          return o;
      }
    }
    else {
      Object o = obj;
      if ((o instanceof Page)) {
        return obj;
      }
      try
      {
        Method m = o.getClass().getMethod("usePage", new Class[0]);
        if (m != null) {
          Class clz = m.getReturnType();
          if (clz.getName().equals(Page.class.getName()))
            return m.invoke(o, new Object[0]);
        }
      } catch (SecurityException e) {
      } catch (NoSuchMethodException e) {
      } catch (IllegalArgumentException e) {
      }
      catch (IllegalAccessException e) {
      }
      catch (InvocationTargetException e) {
      }
    }
    return null;
  }

  public static class BoundSqlSqlSource
    implements SqlSource
  {
    BoundSql boundSql;

    public BoundSqlSqlSource(BoundSql boundSql)
    {
      this.boundSql = boundSql;
    }

    public BoundSql getBoundSql(Object parameterObject) {
      return this.boundSql;
    }
  }
}