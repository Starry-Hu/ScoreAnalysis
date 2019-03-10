package com.scoreanalysis.service;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.List;

public interface ExcelUploadService {
    List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception;

    Workbook getWorkbook(InputStream inStr, String fileName) throws Exception;
}
