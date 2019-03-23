package com.scoreanalysis.controller;

import com.scoreanalysis.bean.Plan;
import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.service.PlanService;
import com.scoreanalysis.util.BaseResponse;
import com.scoreanalysis.util.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public BaseResponse addPlan(String planName) throws Exception {
        // 信息填写不完全
        if (planName == null || planName.trim().equals("")) {
            return ajaxFail(ResultEnum.PLAN_INFO_NOT_FULL);
        }

        // 添加教学计划
//        Plan plan = planService.addPlanInfo(planName);

        return ajaxSucc(12, ResultEnum.PLAN_ADD_SUCCESS);
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
    public BaseResponse addPlanCourses(MultipartFile file, String planName) throws Exception {
        // 信息填写不完全
        if (file == null || planName == null || planName.trim().equals("")) {
            return ajaxFail(ResultEnum.PLAN_INFO_NOT_FULL);
        }
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        // 检查格式是否正确
        boolean isValidate = excelImportUtil.validateExcel(fileName);
        if (!isValidate) {
            return ajaxFail(ResultEnum.EXCEL_FORM_ERROR);
        }

        // 添加教学计划基本信息
        // 对planCourse关系表进行处理
        planService.batchUpload(file, planName);
        // 教学计划对应课程关系导入成功
        return ajaxSucc(null, ResultEnum.PLAN_ADD_SUCCESS);
    }


    /**
     * @Description: 删除教学计划和教学计划课程对应关系
     * @Param: [planId]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/19
     */
    @GetMapping("/deleteRelatedByPid")
    public BaseResponse deletePlanRelated(String planId) throws Exception {
        // 信息填写不完全
        if (planId == null || planId.trim().equals("")) {
            return ajaxFail(ResultEnum.PLAN_INFO_NOT_FULL);
        }

        planService.deletePlanRelated(planId);

        return ajaxSucc(null, ResultEnum.PLAN_DELETE_SUCCESS);
    }

    /**
     * @Description: 删除全部教学计划和教学计划课程对应关系
     * @Param: []
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: StarryHu
     * @Date: 2019/3/22
     */
    @GetMapping("/deleteAllRelated")
    public BaseResponse deleteAllPlansRelated() throws Exception {
        planService.deleteAllPlansRelated();

        return ajaxSucc(null, ResultEnum.PLAN_DATA_DELETE_SUCC);
    }

    /** 
    * @Description: 获取全部的教学计划基本信息
    * @Param: [] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/23 
    */ 
    @GetMapping("/getAllPlans")
    public BaseResponse getAllPlans() throws Exception{
        List<Plan> planList = planService.getAllPlans();

        return ajaxSucc(planList,ResultEnum.PLAN_SEARCH_SUCCESS);
    }
    
    
}
