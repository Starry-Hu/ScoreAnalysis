package com.scoreanalysis.controller;

import com.scoreanalysis.bean.Plan;
import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.service.PlanService;
import com.scoreanalysis.util.BaseResponse;
import com.scoreanalysis.util.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Project scoreanalysis
 * @ClassName PlanController
 * @Author StarryHu
 * @Description 教学计划相关接口
 * @Date 2019/3/18 19:58
 */

@RestController
@RequestMapping("plan")
public class PlanController extends BaseController {
    @Autowired
    private PlanService planService;
    private ExcelImportUtil excelImportUtil = new ExcelImportUtil();

    /**
    * @Description: 添加教学计划基本信息
    * @Param: [planName, planYear]
    * @return: com.scoreanalysis.util.BaseResponse
    * @Author: StarryHu
    * @Date: 2019/3/18
    */
    @PostMapping("/addBasic")
    public BaseResponse addPlan(String planName,String planYear) throws Exception{
        // 信息填写不完全
        if (planName == null || planYear == null ||
                planName.trim().equals("") || planYear.trim().equals("")){
            return ajaxFail(ResultEnum.PLAN_INFO_NOT_FULL);
        }

        // 添加教学计划
        Plan plan = planService.addPlan(planName,planYear);

        return ajaxSucc(plan,ResultEnum.PLAN_ADD_SUCCESS);
    }

    /** 
    * @Description: 添加教学计划——课程关系信息(上传文件)
    * @Param: [file, planId] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/18 
    */
    @ResponseBody
    @PostMapping("/uploadAddCourses")
    public BaseResponse addPlanCourses(MultipartFile file) throws Exception{
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        // 检查格式是否正确
        boolean isValidate = excelImportUtil.validateExcel(fileName);
        if (!isValidate){
            return ajaxFail(ResultEnum.EXCEL_FORM_ERROR);
        }
        // 判断excel文件类型
        boolean isExcel2003 = excelImportUtil.isExcel2003(fileName);

        String planId = "123";
        // 对planCourse关系表进行处理
        planService.batchUpload(fileName, file, isExcel2003,planId);
        // 教学计划对应课程关系导入成功
        return ajaxSucc(null,ResultEnum.PLAN_ADD_SUCCESS);
    }

    
    /** 
    * @Description: 删除教学计划和教学计划课程对应关系
    * @Param: [planId] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/19 
    */
    @GetMapping("/deleteRelated")
    public BaseResponse deletePlanRelated(String planId) throws Exception{
        // 信息填写不完全
        if (planId == null || planId.trim().equals("")){
            return ajaxFail(ResultEnum.PLAN_INFO_NOT_FULL);
        }

        planService.deletePlanRelated(planId);

        return ajaxSucc(null,ResultEnum.PLAN_DELETE_SUCCESS);
    }
}
