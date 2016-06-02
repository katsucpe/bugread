package com.kem.Export;

import com.kem.WordCountWebDriver.WordCountWeb;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

    public void AddData(List<WordCountWeb.WordDensity> densityList) throws IOException {
        String excelFileName = filePath;//name of excel file



        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFRow rowHeader = sheet.createRow(0);
        rowHeader.createCell(0).setCellValue("Word");
        rowHeader.createCell(1).setCellValue("Blocker");
        //iterating r number of rows
        for (int r = 0; r < densityList.size(); r++) {
            XSSFRow row = sheet.createRow(r + 1);

            XSSFCell wordCell = row.createCell(0);
            wordCell.setCellValue(densityList.get(r).getWord());
            XSSFCell wordDensityCell = row.createCell(1);
            wordDensityCell.setCellValue(densityList.get(r).getDensity());

        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    public void AddData(List<WordCountWeb.WordDensity> densityList, String severity) throws IOException {
        if (Files.exists(Paths.get(filePath))) {
            int rowNum = 0;
            InputStream input = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(input);
            XSSFSheet sheet = wb.getSheetAt(0);
            for (WordCountWeb.WordDensity word:densityList){
                int targetRow = getSearchRow(sheet, word.getWord());
            }


        }
    }

    private int getSearchRow(XSSFSheet sheet, String cellContent){
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            XSSFRow row = sheet.getRow(j);
            XSSFCell cell = row.getCell(0);
            int rowNum = 0;
            if(cell.getRichStringCellValue().getString () == cellContent){

                rowNum = row.getRowNum();
                return rowNum;
            }
        }
        return sheet.getLastRowNum();
    }
}
