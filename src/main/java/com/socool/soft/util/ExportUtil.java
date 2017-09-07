package com.socool.soft.util;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class ExportUtil  
{  
    private Workbook wb = null;  
  
    private  Sheet sheet = null;  
  
    /** 
     * @param wb 
     * @param sheet 
     */  
    public ExportUtil(Workbook wb, Sheet sheet)  
    {  
        this.wb = wb;  
        this.sheet = sheet;  
    }  
  
    /** 
     * 合并单元格后给合并后的单元格加边框 
     *  
     * @param region 
     * @param cs 
     */  
    public void setRegionStyle(CellRangeAddress region, XSSFCellStyle cs)  
    {  
  
        int toprowNum = region.getFirstRow();  
        for (int i = toprowNum; i <= region.getLastRow(); i++)  
        {  
            Row row = sheet.getRow(i);  
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++)  
            {  
                Cell cell = row.getCell(j);// XSSFCellUtil.getCell(row,  
                                                // (short) j);  
                cell.setCellStyle(cs);  
            }  
        }  
    }  
  
    /** 
     * 设置表头的单元格样式 
     *  
     * @return 
     */  
    public CellStyle getHeadStyle()  
    {  
        // 创建单元格样式  
        CellStyle cellStyle = wb.createCellStyle();  
        // 设置单元格的背景颜色为淡蓝色  
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);  
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
        // 设置单元格居中对齐  
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);  
        // 设置单元格垂直居中对齐  
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);  
        // 创建单元格内容显示不下时自动换行  
        cellStyle.setWrapText(true);  
        // 设置单元格字体样式  
        Font font = wb.createFont();  
        // 设置字体加粗  
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);  
        font.setFontName("宋体");  
        font.setFontHeight((short) 200);  
        cellStyle.setFont(font);  
        // 设置单元格边框为细线条  
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);  
        return cellStyle;  
    }  
  
    /** 
     * 设置表体的单元格样式 
     *  
     * @return 
     */  
    public CellStyle getBodyStyle()  
    {  
        // 创建单元格样式  
        CellStyle cellStyle = wb.createCellStyle();  
        // 设置单元格居中对齐  
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);  
        // 设置单元格垂直居中对齐  
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);  
        // 创建单元格内容显示不下时自动换行  
        cellStyle.setWrapText(true);  
        // 设置单元格字体样式  
        Font font = wb.createFont();  
        // 设置字体加粗  
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);  
        font.setFontName("宋体");  
        font.setFontHeight((short) 200);  
        cellStyle.setFont(font);  
        // 设置单元格边框为细线条  
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);  
        return cellStyle;  
    }  
  
}  