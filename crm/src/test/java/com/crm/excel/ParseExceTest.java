package com.crm.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 王琦
 * 2021/6/18
 */
class ParseExcelTest {
    public static void main(String[] args)throws Exception {

        InputStream is = new FileInputStream("d:\\市场活动表.xls");

        //1.工作簿
        HSSFWorkbook wb = new HSSFWorkbook(is);

        HSSFSheet sheet =wb.getSheetAt(0);

        HSSFRow row = null;
        HSSFCell cell = null;

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            sheet.getRow(i);
            for (int j=0;j<row.getLastCellNum();j++){
                cell = row.getCell(j);
                getCellValue(cell);
            }
        }
    }

    //将cell单元格数据传过来判断类型并用合适方法取出后转成字符串
    public static  String getCellValue(HSSFCell cell){
        String ret = "";
        switch (cell.getCellType()){
            case  HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue()+"";
                break;
            default:
                ret="";
        }
            return ret;
    }
}
