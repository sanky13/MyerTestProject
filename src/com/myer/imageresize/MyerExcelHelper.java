package com.myer.imageresize;


import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellFormat;
import jxl.Sheet;
import jxl.Workbook;


import jxl.read.biff.BiffException;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class MyerExcelHelper
{
   public  void writeExcel(String imagePath , String type, double recommendedValue, 
		   double currentValue, double overShootValue, WritableWorkbook  wworkbook , int rowValue , float ratio) 
      throws BiffException, IOException, WriteException
   {
	   WritableSheet wchecksheet =  null;
//      if(MyerConstants.IMAGE_THUMBNAIL.equalsIgnoreCase(type)){
    	   wchecksheet =  verifySheet(wworkbook, type);
//    	   wchecksheet = wworkbook.getSheet("Zoom");
    	   
//    	   wchecksheet = wworkbook.createSheet("Sankar", 1);  
//      }
//    	   System.out.println("worksheet value XLS: " + wchecksheet.getName() + " " + wchecksheet.getRows());
      // Adding the labels //
    	   int rowValuetemp = wchecksheet.getRows();
      if(wchecksheet !=null  && wchecksheet.getRows()< 1){
//    	  wchecksheet = wworkbook.createSheet("sankar", 0);
	      Label label = new Label(0, 0, "Image Path");
	      wchecksheet.addCell(label);
	      label = new Label(1, 0, "Recommended Size");
	      wchecksheet.addCell(label);
	      label = new Label(2, 0, "Current Size");
	      wchecksheet.addCell(label);
	      label = new Label(3, 0, "OverShooting value");
	      wchecksheet.addCell(label);
	      
	      Label rowLable = new Label(0, rowValuetemp+1, imagePath);
	      wchecksheet.addCell(rowLable);
	     Number  numbrow = new Number(1, rowValuetemp+1, recommendedValue);
	      wchecksheet.addCell(numbrow);
	      numbrow = new Number(2, rowValuetemp+1, currentValue);
	      wchecksheet.addCell(numbrow);
	      numbrow = new Number(3, rowValuetemp+1,overShootValue);
	      wchecksheet.addCell(numbrow);
	      
      } else{
//    	  wchecksheet = wworkbook.getSheet("sankar");
	      Label rowLable = new Label(0, rowValuetemp, imagePath);
	      wchecksheet.addCell(rowLable);
		     Number  numbrow = new Number(1, rowValuetemp, recommendedValue);
		     
		      wchecksheet.addCell(numbrow);
		      numbrow = new Number(2, rowValuetemp, currentValue);
		      wchecksheet.addCell(numbrow);
		      numbrow = new Number(3, rowValuetemp,overShootValue);
		      wchecksheet.addCell(numbrow);
      
		     
	      System.out.println("writing the values to the XLS: " + rowValue + " " + imagePath + " " +  wchecksheet.isHidden() );

	      wworkbook.write();
      }
      if(ratio <.5){
//    	  (wchecksheet.getRowView(rowValuetemp)).setFormat(CellFormat.)
      }
      wworkbook.write();
//      Workbook workbook = Workbook.getWorkbook(new File("output.xls"));
//      Sheet sheet = workbook.getSheet(0);
//      Cell cell1 = sheet.getCell(0, 2);
//      System.out.println(cell1.getContents());
//      Cell cell2 = sheet.getCell(3, 4);
//      System.out.println(cell2.getContents());
//      workbook.close();
   }
   
   public  WritableSheet verifySheet(WritableWorkbook wBook, String sheetType) 
   throws BiffException, IOException, WriteException
	{
	   WritableSheet wchecksheet = wBook.getSheet(sheetType);
	   
 	  if( wchecksheet== null && wBook.getSheets().length <= 1){
 		  wchecksheet = wBook.createSheet(sheetType, wBook.getSheets().length+1);  
 	  } 
 	  return wchecksheet;
	}
   
   public static Workbook verifyAndReturnWorkbook( String workBookName) 
   throws BiffException, IOException, WriteException
	{
	   Workbook wworkbook;
	    wworkbook = Workbook.getWorkbook(new File(workBookName+".xls"));
	  
 	  if( wworkbook== null){
 	      
// 	      wworkbook = Workbook.createWorkbook(new File(workBookName +"output.xls"));  
 	  }
 	  return wworkbook;
	}
   
}