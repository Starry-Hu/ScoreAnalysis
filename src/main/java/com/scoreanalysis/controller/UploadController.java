package com.scoreanalysis.controller;

import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.StudentService;
import com.scoreanalysis.util.BaseResponse;
import com.scoreanalysis.util.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * @Project scoreanalysis
 * @ClassName UploadController
 * @Author StarryHu
 * @Description 上传信息的相关接口
 * @Date 2019/3/10 18:14
 */
@RequestMapping("upload")
public class UploadController extends BaseController {
    @Autowired
    private ExcelImportUtil excelImportUtil;
    private StudentService studentService;


    /**
    * @Description: 上传教学计划文件信息
    * @Param: [file, planId]
    * @return: com.scoreanalysis.util.BaseResponse
    * @Author: StarryHu
    * @Date: 2019/3/11
    */
    @RequestMapping(value = "/plansInfo")
    public BaseResponse planInfoUpload(@RequestParam(value = "filename") MultipartFile file, String planId) throws Exception {
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        // 检查格式是否正确
        boolean isValidate = excelImportUtil.validateExcel(fileName);
        if (!isValidate){
            return ajaxFail(ResultEnum.EXCEL_FORM_ERROR);
        }
        // 判断excel文件类型
        boolean isExcel2003 = excelImportUtil.isExcel2003(fileName);


        return null;
        // 对planCourse关系表进行处理
    }

    @RequestMapping(value = "/studentsInfo")
    public String studentsInfoUpload(@RequestParam(value = "filename") MultipartFile file, HttpSession session) throws Exception {
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        // 检查格式是否正确
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new SAException(ExceptionEnum.UPLOAD_FORM_ERROR);
        }
        // 判断excel文件类型
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }

        // 对专业表的处理

        // 对班级表的处理

        // 对学生表的处理
        studentService.batchUpload(fileName, file, isExcel2003);

        return "redirect:index";
    }

}
