package com.kem.Classification;

import com.kem.WordCountWebDriver.WordCountWeb;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KEM-PC on 1/6/2559.
 */
public class ExcelClassResultWriter {
    private final String filePath;
    final String sheetName = "Result";
    private final List<ClassificationByVisualizationResult.ClassifiedObject> resultList;

    public ExcelClassResultWriter(String filePath, List<ClassificationByVisualizationResult.ClassifiedObject> resultList) {
        this.filePath = filePath;
        this.resultList = resultList;
    }
    Logger logger = Logger.getLogger(ExcelClassResultWriter.class);

    public void writeDataToFile() throws IOException {
        XSSFWorkbook wb;
        XSSFSheet sheet;
        if (Files.exists(Paths.get(filePath))) {
            InputStream input = new FileInputStream(filePath);
            wb = new XSSFWorkbook(input);
            sheet = wb.getSheetAt(0);

        }else{
            wb = new XSSFWorkbook();
            sheet = wb.createSheet(sheetName);
            XSSFRow rowHeader = sheet.createRow(0);
            rowHeader.createCell(0).setCellValue("ID");
            rowHeader.createCell(1).setCellValue("Subject");
            rowHeader.createCell(2).setCellValue("Actual");
            rowHeader.createCell(3).setCellValue("Class");
            rowHeader.createCell(4).setCellValue("correct? - 6 class");
            rowHeader.createCell(5).setCellValue("correct? - 2 class");
        }
        List<String> lowservList = Arrays.asList( new String[]{"normal", "minor", "trivial"});
        List<String> highservList = Arrays.asList( new String[]{"blocker", "critical", "major"});
        for (ClassificationByVisualizationResult.ClassifiedObject result:resultList) {

            XSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            newRow.createCell(0).setCellValue(result.getId());
            newRow.createCell(1).setCellValue(result.getSubject());
            newRow.createCell(2).setCellValue(result.getActual());
            newRow.createCell(3).setCellValue(result.getClassResult());
            if(result.getClassResult().isEmpty()){
                newRow.createCell(4).setCellValue("N/A");
                newRow.createCell(5).setCellValue("N/A");
            } else {
                if (result.getClassResult().equals(result.getActual())) {
                    newRow.createCell(4).setCellValue("T");
                    newRow.createCell(5).setCellValue("T");
                } else if (result.getClassResult().equalsIgnoreCase("high") && highservList.contains(result.getActual().toLowerCase())) {
                    newRow.createCell(4).setCellValue("");
                    newRow.createCell(5).setCellValue("T");
                } else if (result.getClassResult().equalsIgnoreCase("low") && lowservList.contains(result.getActual().toLowerCase())) {
                    newRow.createCell(4).setCellValue("");
                    newRow.createCell(5).setCellValue("T");
                } else if(highservList.contains(result.getClassResult().toLowerCase()) && highservList.contains(result.getActual().toLowerCase())){
                    newRow.createCell(4).setCellValue("F");
                    newRow.createCell(5).setCellValue("T");
                } else if(lowservList.contains(result.getClassResult().toLowerCase()) && lowservList.contains(result.getActual().toLowerCase())){
                    newRow.createCell(4).setCellValue("F");
                    newRow.createCell(5).setCellValue("T");
                }else
                    {
                    newRow.createCell(4).setCellValue("F");
                    newRow.createCell(5).setCellValue("F");
                }
            }
        }

        XSSFRow rowHeader = sheet.getRow(1);
        int lastRow = resultList.size() + 1;
        rowHeader.createCell(8).setCellFormula("COUNTIF(E2:E" + lastRow + ", \"T\")");
        rowHeader.createCell(9).setCellFormula("COUNTIF(E2:E" + lastRow + ", \"F\")");
        rowHeader.createCell(10).setCellFormula("COUNTIF(E2:E" + lastRow + ", \"N/A\")");
        XSSFRow rowHeader2 = sheet.getRow(2);
        rowHeader2.createCell(8).setCellFormula("COUNTIF(F2:F" + lastRow + ", \"T\")");
        rowHeader2.createCell(9).setCellFormula("COUNTIF(F2:F" + lastRow + ", \"F\")");
        rowHeader2.createCell(10).setCellFormula("COUNTIF(F2:F" + lastRow + ", \"N/A\")");


        FileOutputStream fileOut = new FileOutputStream(filePath);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }



}
