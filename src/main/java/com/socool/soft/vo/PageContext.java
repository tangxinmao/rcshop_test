package com.socool.soft.vo;

import javax.servlet.http.HttpServletRequest;

public class PageContext
{
  private static ThreadLocal<Page> pageContext = new ThreadLocal<Page>();
  public static final String PARAM_NAME_PAGE_NO = "pgn";
  public static final int DEFAULT_PAGE_SIZE = 10;

  public static void setPage(Page page)
  {
    pageContext.set(page);
  }

  public static Page getPage()
  {
    return (Page)pageContext.get();
  }

  public static void removePage()
  {
    pageContext.remove();
  }

  public static void setPageViaRequest(HttpServletRequest request, int pageSize)
  {
    String noString = request.getParameter("pgn");
    int currentNo = 1;
    if (noString != null)
      try {
        currentNo = Integer.parseInt(noString);
      }
      catch (NumberFormatException e) {
      }
    Page page = new Page(currentNo);
    page.setPagination(true);
    page.setPageSize(pageSize);

    setPage(page);
    request.setAttribute("page", page);
  }

  public static void setPageViaRequest(HttpServletRequest request)
  {
    setPageViaRequest(request, 10);
  }
}