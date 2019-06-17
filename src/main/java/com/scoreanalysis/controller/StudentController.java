package com.scoreanalysis.controller;

import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.pojo.StuInfoExtend;
import com.scoreanalysis.service.StudentService;
import com.scoreanalysis.util.BaseResponse;
import com.scoreanalysis.util.ExcelImportUtil;
import com.scoreanalysis.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
    public BaseResponse addStudentInfo(MultipartFile file, String planId) throws Exception {
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
        studentService.batchUpload(file, planId);

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
    public BaseResponse deleteStusRelated() throws Exception {
        // 删除全部学生相关信息
        studentService.deleteAllStusRelated();

        return ajaxSucc(null, ResultEnum.STUDENT_DATA_DELETE_SUCC);
    }


    // ----------------------------------------------------分页相关------------------------------------------------------

    /**
     * @Description: 根据学生班级获取全部学生的修课情况（返回为学生信息扩展对象数组）[带分页]
     * @Param: [clsId, pageNum, pageSize]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    @GetMapping("/getStuInfoByStuClsWithPage")
    public BaseResponse getStuInfoByStuClsWithPage(String clsId, Integer pageNum, Integer pageSize) throws Exception {
        // 判断班级id是否填写,若未填写则赋默认值
        if (clsId == null || clsId.trim().equals("")) {
            clsId = "021101";
        }
        // 处理分页的默认情况
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        PageBean<StuInfoExtend> data = studentService.getStuInfoByStuClsWithPage(clsId, pageNum, pageSize);
        return ajaxSucc(data, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }

    /**
     * @Description: 根据专业获取全部学生的修课情况（返回为学生信息扩展对象数组）[带分页]
     * @Param: [majorId]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    @GetMapping("/getStuInfoByMajorWithPage")
    public BaseResponse getStuInfoByMajorWithPage(String majorId, Integer pageNum, Integer pageSize) throws Exception {
        // 判断专业是否填写
        if (majorId == null || majorId.trim().equals("")) {
            majorId = "02";
        }
        // 处理分页默认情况
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }

        PageBean<StuInfoExtend> data = studentService.getStuInfoByMajorWithPage(majorId, pageNum, pageSize);
        return ajaxSucc(data, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }

    /**
     * @Description: 获取全部学生的修课情况（返回为学生信息扩展对象数组）[带分页]
     * @Param: [pageNum, pageSize]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/28
     */
    @GetMapping("/getAllStuInfoWithPage")
    public BaseResponse getAllStuInfoWithPage(String sid ,Integer pageNum, Integer pageSize) throws Exception {
        // 如果传入了学号id，则直接查找这个学生情况
        if (sid != null && !sid.trim().equals("")){
            PageBean<StuInfoExtend> data  = studentService.getStuInfoBySid(sid,pageNum,pageSize);
            return  ajaxSucc(data,ResultEnum.STUDENT_SEARCH_SUCCESS);
        }

        // 在全部学生范围内查找
        // 处理分页默认情况
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        PageBean<StuInfoExtend> data = studentService.getAllStuInfoWithPage(pageNum,pageSize);
        return ajaxSucc(data, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }

    // ----------------------------------------------------英语相关------------------------------------------------------

    /**
    * @Description: 根据学生班级获取全部学生的大英4情况（返回为学生信息扩展对象数组）[带分页]
    * @Param: [clsId, pageNum, pageSize]
    * @return: com.scoreanalysis.util.BaseResponse
    * @Author: StarryHu
    * @Date: 2019/3/29
    */
    @GetMapping("/getStuInfoByClsIdWithPageInEng4")
    public BaseResponse getStuInfoByClsIdWithPageInEng4(String clsId, Integer pageNum, Integer pageSize) throws Exception {
        // 判断班级id是否填写,若未填写则赋默认值
        if (clsId == null || clsId.trim().equals("")) {
            clsId = "021101";
        }
        // 处理分页的默认情况
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        PageBean<StuInfoExtend> data = studentService.getStuInfoByClsIdWithPageInEng4(clsId, pageNum, pageSize);
        return ajaxSucc(data, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }


    /**
     * @Description: 根据学生班级获取全部学生的大英4情况（返回为学生信息扩展对象数组）[带分页]
     * @Param: [clsId, pageNum, pageSize]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/29
     */
    @GetMapping("/getStuInfoByMajorWithPageInEng4")
    public BaseResponse getStuInfoByMajorWithPageInEng4(String majorId, Integer pageNum, Integer pageSize) throws Exception {
        // 判断专业id是否填写,若未填写则赋默认值
        if (majorId == null || majorId.trim().equals("")) {
            majorId = "02";
        }
        // 处理分页的默认情况
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        PageBean<StuInfoExtend> data = studentService.getStuInfoByMajorWithPageInEng4(majorId,pageNum,pageSize);
        return ajaxSucc(data, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }


    /**
    * @Description: 获取全部学生的大英4情况（返回为学生信息扩展对象数组）[带分页]
    * @Param: [sid, pageNum, pageSize]
    * @return: com.scoreanalysis.util.BaseResponse
    * @Author: StarryHu
    * @Date: 2019/3/29
    */
    @GetMapping("/getAllStuInfoWithPageInEng4")
    public BaseResponse getAllStuInfoWithPageInEng4(String sid ,Integer pageNum, Integer pageSize) throws Exception {
        // 如果传入了学号id，则直接查找这个学生情况
        if (sid != null && !sid.trim().equals("")){
            PageBean<StuInfoExtend> data  = studentService.getStuInfoBySidInEng4(sid);
            return  ajaxSucc(data,ResultEnum.STUDENT_SEARCH_SUCCESS);
        }

        // 在全部学生范围内查找
        // 处理分页默认情况
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        PageBean<StuInfoExtend> data = studentService.getAllStuInfoWithPageInEng4(pageNum,pageSize);
        return ajaxSucc(data, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }


    // ---------------------------------------- 通知相关 --------------------------------------------
    /*** 
    * @Description: 根据学号获取该学生的联系方式
    * @Param: [sid] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/6/17 
    */ 
    @GetMapping("/getStuInformWay")
    public BaseResponse getStuInform(String sid) throws Exception {
        // 判断学号是否填写
        if (sid == null || sid.trim().equals("")) {
            return ajaxFail(ResultEnum.STUDENT_INFO_NOT_FULL);
        }

        Map<String,String> resultMap = studentService.getStuInformWay(sid);
        return ajaxSucc(resultMap,ResultEnum.INFORM_WAY_SEARCH_SUCCESS);
    }

    /*** 
    * @Description: 对学生发送邮件进行通知 
    * @Param: [toEmail, informContent] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/6/17 
    */ 
    @PostMapping("/sendMailInform")
    public BaseResponse sendMailInform(String toEmail,String informContent) throws Exception {
        // 如果学生的邮箱信息为空，则抛出异常
        if (toEmail == null || toEmail.equals("")){
            return ajaxFail(ResultEnum.INFORM_EMAIL_NULL);
        }
        // 如果代码忘记配置通知内容 - 供后期维护
        if (informContent == null ||informContent.equals("")){
            return ajaxFail(ResultEnum.INFORM_CONTENT_NULL);
        }
        String fromEmail = "1756487384@qq.com";

        studentService.sendMailInform(fromEmail,toEmail,informContent);

        return ajaxSucc(null,ResultEnum.INFORM_EMAIL_SUCCESS);
    }

    // ---------------------------------------- 单个功能 未与分页相关联  暂时搁置--------------------------------------------
    /**
     * @Description: 根据学号获取该学生的修课情况（返回为学生信息扩展对象）
     * @Param: [sid]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/23
     */
    @GetMapping("/getStuInfoBySid")
    public BaseResponse getStuInfoBySid(String sid) throws Exception {
        // 判断学号是否填写
        if (sid == null || sid.trim().equals("")) {
            return ajaxFail(ResultEnum.STUDENT_INFO_NOT_FULL);
        }

        StuInfoExtend stuInfoExtend = studentService.getStuInfoBySid(sid);
        return ajaxSucc(stuInfoExtend, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }

    /**
     * @Description: 根据学生班级获取全部学生的修课情况（返回为学生信息扩展对象数组）
     * @Param: [clsId]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    @GetMapping("/getStuInfoByStuCls")
    public BaseResponse getStuInfoByStuCls(String clsId, Integer pageNum, Integer pageSize) throws Exception {
        // 判断学号是否填写
        if (clsId == null || clsId.trim().equals("")) {
            return ajaxFail(ResultEnum.STUDENT_INFO_NOT_FULL);
        }

        List<StuInfoExtend> data = studentService.getStuInfoByStuCls(clsId);
        return ajaxSucc(data, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }

    /**
     * @Description: 根据专业获取全部学生的修课情况（返回为学生信息扩展对象数组）
     * @Param: [majorId]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    @GetMapping("/getStuInfoByMajor")
    public BaseResponse getStuInfoByMajor(String majorId) throws Exception {
        // 判断专业id是否填写
        if (majorId == null || majorId.trim().equals("")) {
            return ajaxFail(ResultEnum.STUDENT_INFO_NOT_FULL);
        }
        List<StuInfoExtend> data = studentService.getStuInfoByMajor(majorId);
        return ajaxSucc(data, ResultEnum.STUDENT_SEARCH_SUCCESS);
    }



}
