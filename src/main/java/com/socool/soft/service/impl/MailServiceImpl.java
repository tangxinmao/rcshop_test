package com.socool.soft.service.impl;

import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.constant.PropertyConstants;
import com.socool.soft.service.IMailService;
import com.socool.soft.vo.MailVo;
import com.sun.mail.util.MailSSLSocketFactory;
@Service
public class MailServiceImpl implements IMailService {
	@Autowired
	private PropertyConstants propertyConstants;

	@Override
	public void sendMail(MailVo mailVo) throws GeneralSecurityException, AddressException, MessagingException {
	
	      // 获取系统属性
	      Properties properties = System.getProperties();
	      MailSSLSocketFactory sf = new MailSSLSocketFactory();
	      sf.setTrustAllHosts(true);
	      properties.put("mail.smtp.ssl.enable", "true");
	      properties.put("mail.smtp.ssl.socketFactory", sf);
	      // 设置邮件服务器
	      properties.setProperty("mail.smtp.host", propertyConstants.mailHost);
	      properties.put("mail.smtp.auth", "true");
	      // 获取默认session对象
	      Session session = Session.getInstance(properties,new Authenticator(){
		    public PasswordAuthentication getPasswordAuthentication()
		    {
		     return new PasswordAuthentication(propertyConstants.mailFrom, propertyConstants.mailAuth); //发件人邮件用户名、密码
		    }
		   });

	         // 创建默认的 MimeMessage 对象
	         MimeMessage message = new MimeMessage(session);
	         // Set From: 头部头字段
	         message.setFrom(new InternetAddress(propertyConstants.mailFrom));

	         // Set To: 头部头字段
	         message.addRecipient(Message.RecipientType.TO,new InternetAddress(mailVo.getTo()));

	         // Set Subject: 头部头字段
	         message.setSubject(mailVo.getSubject());
	         // 设置消息体
	         String realPath = null ;
           if(mailVo.getHttpServletRequest()!=null)
          	realPath = mailVo.getHttpServletRequest().getSession().getServletContext().getRealPath("/");
	         Properties p = new Properties();
	         if(realPath==null)
	         {
	        	 String classPath = this.getClass().getClassLoader().getResource("").getPath();
	        	 realPath = classPath.substring(0, classPath.indexOf("WEB-INF"));
	         }
	         p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, realPath+"WEB-INF/views/velocity/template");
	         Velocity.init(p);
	        // VelocityContext context = new VelocityContext();
	         StringWriter writer = new StringWriter();
	         org.apache.velocity.Template template = Velocity.getTemplate(mailVo.getVelocity());
	         //资源绝对访问域名
	         if(mailVo.getHttpServletRequest()!=null)
	         mailVo.getContext().put("root", "http://"+ mailVo.getHttpServletRequest().getServerName()+ mailVo.getHttpServletRequest().getContextPath());
	      
	        	  
	         template.merge(mailVo.getContext(), writer);
	         String s = writer.toString();
	         message.setContent(s,
	        		 "text/html;charset=UTF-8" );
	         // 发送消息
	         Transport.send(message);
	         System.out.println("Sent message successfully");
	}

}
