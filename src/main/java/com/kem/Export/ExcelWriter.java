package com.kem.Export;

import com.kem.WordCountWebDriver.WordCountWeb;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by KEM-PC on 1/6/2559.
 */
public class ExcelWriter {
    private final String filePath;

    public ExcelWriter(String filePath) {
        this.filePath = filePath;
    }

    public void AddData(List<WordCountWeb.WordDensity> densityList) throws IOException {
        String excelFileName = filePath;//name of excel file

        String sheetName = "Sheet1";//name of sheet

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
}
