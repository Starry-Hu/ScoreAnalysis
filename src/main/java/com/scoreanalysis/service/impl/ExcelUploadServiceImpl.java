package com.scoreanalysis.service.impl;

import com.scoreanalysis.service.ExcelUploadService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @Project scoreanalysis
 * @ClassName ExcelUploadServiceImpl
 * @Author 葫芦胡
 * @Description excel表格上传的相关业务逻辑层
 * @Date 2019/2/10 23:37
 */

@Service("excelUploadService")
public class ExcelUploadServiceImpl implements ExcelUploadService {
    private final static String excel2003 = ".xls";
    private final static String excel2007 = ".xlsx";


    /** 
    * @Description: 处理上传的excel文件
    * @Param: [in, fileName] 
    * @return: java.util.List<java.util.List<java.lang.Object>> 
    * @Author: 葫芦胡
    * @Date: 2019/2/10 
    */ 
    public List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
        List<List<Object>> list = null;
        //创建Excel工作薄
        Workbook work = this.getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<List<Object>>();
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                List<Object> li = new ArrayList<Object>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(cell);
                }
                list.add(li);
            }
        }

        work.close();
        return list;
    }

    /** 
    * @Description: 判断excel文件的格式
    * @Param: [inStr, fileName]
    * @return: org.apache.poi.ss.usermodel.Workbook
    * @Author: 葫芦胡
    * @Date: 2019/2/10
    */
    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003.equals(fileType)) {
            wb = new HSSFWorkbook(inStr);
        } else if (excel2007.equals(fileType)) {
            wb = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

}
