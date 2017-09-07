package com.socool.soft.vo;

import java.io.Serializable;

public class Page
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int pageSize;
  private int currentPage;
  private long totalRows;
  private boolean pagination = false;

  public void setPageSize(int pageSize)
  {
    this.pageSize = pageSize;
  }

  public void setCurrentPage(int currentPage)
  {
    this.currentPage = currentPage;
  }

  public Page()
  {
    this(1, 10);
  }

  public Page(int currentPage)
  {
    this(currentPage, 10);
  }

  public Page(int currentPage, int pageSize)
  {
    this(currentPage, pageSize, 0L);
  }

  public Page(int currentPage, int pageSize, long totalRows)
  {
    this.currentPage = currentPage;
    this.pageSize = pageSize;
    this.totalRows = totalRows;
    this.pagination = true;
  }

  public int getCurrentPage()
  {
    return this.currentPage;
  }

  public int getPageSize()
  {
    return this.pageSize;
  }

  public int getTotalPages()
  {
//	System.out.println("totalRows:"+this.totalRows);
//	System.out.println("getPageSize:"+getPageSize());
//	System.out.println("===>>"+(int)Math.ceil((float)this.totalRows / (float)getPageSize()));
    return getPageSize() == 0 ? 0 : (int)Math.ceil((float)this.totalRows / (float)getPageSize());
  }

  public long getTotalRows()
  {
    return this.totalRows;
  }

  public boolean isHasNextPage()
  {
    return getCurrentPage() < getTotalPages();
  }

  public boolean isFirstPage()
  {
    return !isHasNextPage();
  }

  public boolean isLastPage()
  {
    return !isHasNextPage();
  }

  public long getPageStartRow()
  {
    return (this.currentPage - 1) * this.pageSize;
  }

  public boolean isPagination()
  {
    return this.pagination;
  }

  public void setPagination(boolean pagination)
  {
    this.pagination = pagination;
  }

  public void setTotalRows(long totalRows)
  {
    this.totalRows = totalRows;
  }

  public boolean isHasPreviousPage()
  {
    return this.currentPage > 1;
  }
}