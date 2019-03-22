package com.scoreanalysis.controller;

import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.service.StudentService;
import com.scoreanalysis.util.BaseResponse;
import com.scoreanalysis.util.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Project scoreanalysis
 * @ClassName StudentController
 * @Author StarryHu
 * @Description 学生相关接口
 * @Date 2019/3/10 18:12
 */
@RestController
@RequestMapping("student")
public class StudentController extends BaseController {
    @Autowired
    private StudentService studentService;
    private ExcelImportUtil excelImportUtil = new ExcelImportUtil();

    /** 
    * @Description: 上传学生成绩信息，导入相应的学生基本信息、班级信息以及专业信息
    * @Param: [file] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/22 
    */
    @ResponseBody
    @PostMapping("/uploadStudentInfo")
    public BaseResponse addStudentInfo(MultipartFile file) throws Exception {
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        // 检查格式是否正确
        boolean isValidate = excelImportUtil.validateExcel(fileName);
        if (!isValidate) {
            return ajaxFail(ResultEnum.EXCEL_FORM_ERROR);
        }
        // 判断excel文件类型
        boolean isExcel2003 = excelImportUtil.isExcel2003(fileName);

        // 导入学生班级专业信息
        studentService.batchUpload(fileName, file, isExcel2003);

        // 学生班级专业 信息导入成功
        return ajaxSucc(null, ResultEnum.STUDENT_ADD_SUCCESS);
    }

}
