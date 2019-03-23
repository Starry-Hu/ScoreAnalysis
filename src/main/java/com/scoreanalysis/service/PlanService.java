package com.scoreanalysis.service;

import com.scoreanalysis.bean.Plan;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlanService {
    void batchUpload(MultipartFile file,String planId) throws Exception;

    int deletePlanRelated(String planId) throws Exception;

    int deleteAllPlansRelated() throws Exception;

    List<Plan> getAllPlans() throws Exception;
}
