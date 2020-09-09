package com.stockmarket.datasheet.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.stockmarket.datasheet.model.StockPrice;

public class ExcelHelper {
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  static String[] HEADERs = { "CompanyName", "StockExchange","Price","Date","Time" };
  static String SHEET = "Sheet1";

  public static boolean hasExcelFormat(MultipartFile file) {
	  /*
    if (!TYPE.equals(file.getContentType())) {
      return false;
    }*/

    return true;
  }

  public static List<StockPrice> excelToTutorials(InputStream is) {
	  System.out.println("hello 6");
    try {
    	System.out.println("hello 7");
      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet(SHEET);
      Iterator<Row> rows = sheet.iterator();

      List<StockPrice> tutorials = new ArrayList<StockPrice>();

      int rowNumber = 0;
      while (rows.hasNext()) {
    	  System.out.println("hello 8");
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();

        StockPrice tutorial = new StockPrice();

        int cellIdx = 0;
        System.out.println("hello 9");
        while (cellsInRow.hasNext()) {
        	
          Cell currentCell = cellsInRow.next();

          switch (cellIdx) {
          case 0:
        	  //System.out.println("hello 10");
        	 System.out.println(currentCell.getNumericCellValue());
        	 //System.out.println(currentCell.getStringCellValue());
            tutorial.setCompanyid((long) currentCell.getNumericCellValue());
            break;

          case 1:
        	System.out.println(currentCell.getDateCellValue()); 
            tutorial.setDate(currentCell.getDateCellValue());
            break;

          case 2:
        	System.out.println(currentCell.getNumericCellValue());
            tutorial.setPrice((long) currentCell.getNumericCellValue());
            break;

          case 3:
        	System.out.println(currentCell.getStringCellValue());
            tutorial.setStockexchange(currentCell.getStringCellValue());
            break;
            
          case 4:
          	System.out.println(currentCell.getStringCellValue());
              tutorial.setTime(currentCell.getStringCellValue());
              break;

          default:
            break;
          }

          cellIdx++;
        }

        tutorials.add(tutorial);
      }

      workbook.close();

      return tutorials;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }
}

