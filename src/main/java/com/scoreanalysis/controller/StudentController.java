package com.scoreanalysis.controller;

import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.pojo.StuInfoExtend;
import com.scoreanalysis.service.StudentService;
import com.scoreanalysis.util.BaseResponse;
import com.scoreanalysis.util.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public BaseResponse addStudentInfo(MultipartFile file,String planId) throws Exception {
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
        studentService.batchUpload(file,planId);

        // 学生班级专业 信息导入成功
        return ajaxSucc(null, ResultEnum.STUDENT_DATA_ADD_SUCC);
    }

    /** 
    * @Description: 删除全部学生信息的相关内容（删除学生表、班级表和专业表）
    * @Param: [] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/22 
    */ 
    @GetMapping("/deleteAllRelated")
    public BaseResponse deleteStusRelated() throws Exception{
        // 删除全部学生相关信息
        studentService.deleteAllStusRelated();

        return ajaxSucc(null,ResultEnum.STUDENT_DATA_DELETE_SUCC);
    }

    /**
    * @Description: 根据学号获取该学生的修课情况（返回为学生信息扩展对象）
    * @Param: [sid]
    * @return: com.scoreanalysis.util.BaseResponse
    * @Author: StarryHu
    * @Date: 2019/3/23
    */
    @GetMapping("/getStuInfoBySid")
    public BaseResponse getStuInfoBySid(String sid) throws Exception{
        // 判断学号是否填写
        if (sid == null || sid.trim().equals("")){
            return ajaxFail(ResultEnum.STUDENT_INFO_NOT_FULL);
        }

        StuInfoExtend stuInfoExtend = studentService.getStuInfoBySid(sid);
        return ajaxSucc(stuInfoExtend,ResultEnum.STUDENT_SEARCH_SUCCESS);
    }

    /**
    * @Description: 根据学生班级获取全部学生的修课情况（返回为学生信息扩展对象数组）
    * @Param: [clsId]
    * @return: com.scoreanalysis.util.BaseResponse
    * @Author: StarryHu
    * @Date: 2019/3/24
    */
    @GetMapping("/getStuInfoByStuCls")
    public BaseResponse getStuInfoByStuCls(String clsId) throws Exception{
        // 判断学号是否填写
        if (clsId == null || clsId.trim().equals("")){
            return ajaxFail(ResultEnum.STUDENT_INFO_NOT_FULL);
        }

        List<StuInfoExtend> stuInfoExtendList = studentService.getStuInfoByStuCls(clsId);
        return ajaxSucc(stuInfoExtendList,ResultEnum.STUDENT_SEARCH_SUCCESS);
    }

    /** 
    * @Description: 根据专业获取全部学生的修课情况（返回为学生信息扩展对象数组）
    * @Param: [majorId] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/24 
    */ 
    @GetMapping("/getStuInfoByMajor")
    public BaseResponse getStuInfoByMajor(String majorId) throws Exception{
        // 判断学号是否填写
        if (majorId == null || majorId.trim().equals("")){
            return ajaxFail(ResultEnum.STUDENT_INFO_NOT_FULL);
        }

        List<StuInfoExtend> stuInfoExtendList = studentService.getStuInfoByMajor(majorId);
        return ajaxSucc(stuInfoExtendList,ResultEnum.STUDENT_SEARCH_SUCCESS);
    }
}
