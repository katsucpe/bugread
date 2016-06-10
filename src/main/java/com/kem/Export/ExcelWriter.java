package com.kem.Export;

import com.kem.WordCountWebDriver.WordCountWeb;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by KEM-PC on 1/6/2559.
 */
public class ExcelWriter {
    private final String filePath;
    final String sheetName = "Sheet1";
    public ExcelWriter(String filePath) {
        this.filePath = filePath;
    }
    Logger logger = Logger.getLogger(ExcelWriter.class);

    public void AddData(List<WordCountWeb.WordDensity> densityList, String severity) throws IOException {
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
            rowHeader.createCell(0).setCellValue("Word");
            rowHeader.createCell(1).setCellValue("Blocker");
            rowHeader.createCell(2).setCellValue("Critical");
            rowHeader.createCell(3).setCellValue("Major");
            rowHeader.createCell(4).setCellValue("Normal");
            rowHeader.createCell(5).setCellValue("Minor");
            rowHeader.createCell(6).setCellValue("Trivial");

        }
        for (WordCountWeb.WordDensity word:densityList){
            int targetRow = getSearchRow(sheet, word.getWord());
            if (targetRow > sheet.getLastRowNum()){
                sheet.createRow(targetRow).createCell(0).setCellValue(word.getWord());
            }
            int columnIndex = getColumnIndex(severity);
            logger.info(String.format("Write[%s] to row [%s], and [%s] to column [%s]", word.getWord(), targetRow, word.getDensity(), columnIndex));
            XSSFCell wordCell = sheet.getRow(targetRow).createCell(columnIndex);
            wordCell.setCellValue(word.getDensity());
        }
        FileOutputStream fileOut = new FileOutputStream(filePath);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    private int getSearchRow(XSSFSheet sheet, String cellContent){
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            XSSFRow row = sheet.getRow(j);
            XSSFCell cell = row.getCell(0);
            int rowNum = 0;
            if(cell.getRichStringCellValue().getString().equals(cellContent)){
                rowNum = row.getRowNum();
                return rowNum;
            }
        }
        return sheet.getLastRowNum() + 1;
    }

    private int getColumnIndex(String severity){
        switch (severity.toLowerCase()){
            case "blocker" : return 1;
            case "critical" : return 2;
            case "major" : return 3;
            case "normal" : return 4;
            case "minor" : return 5;
            case "trivial" : return 6;
            default: return 0;
        }
    }

}
