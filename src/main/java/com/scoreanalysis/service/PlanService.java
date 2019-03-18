package com.scoreanalysis.service;

import com.scoreanalysis.bean.Plan;
import org.springframework.web.multipart.MultipartFile;

public interface PlanService {
    Plan addPlan(String planName, String planYear) throws Exception;

    int deletePlan(String planId) throws Exception;

    int updatePlan(String planId,String planName,String planYear) throws Exception;

    boolean batchUpload(String fileName, MultipartFile file, boolean isExcel2003,String planId) throws Exception;

    int deletePlanCourseByPlanId(String planId, String cid) throws Exception;
}
