package com.socool.soft.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;

/**
 * Created by tangxinmao on 2017/3/29.
 */
public class CSVUtil {

    public static void main(String[] args) {


    }

    /**
     * 读取csv
     *
     * @param bufferedReader
     * @return
     * @throws IOException
     */
  public static CSVParser readCsv(BufferedReader bufferedReader) throws IOException {
        CSVParser records = CSVFormat.EXCEL.parse(bufferedReader);
        return records;
    }

    /**
     * 导出csv
     *
     * @param printWriter
     * @throws IOException
     */
   public static void writeCsv(PrintWriter printWriter) throws IOException {
        CSVPrinter csvPrinter = CSVFormat.EXCEL.print(printWriter);
        csvPrinter.printRecord("id", "name");
        csvPrinter.printRecord("sdf", "sdfdf");
        IOUtils.closeQuietly(csvPrinter);
    }

}
