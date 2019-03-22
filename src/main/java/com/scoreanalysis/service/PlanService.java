package com.scoreanalysis.service;

import org.springframework.web.multipart.MultipartFile;

public interface PlanService {
    String addPlanInfo(String planName) throws Exception;

    void batchUpload(MultipartFile file,String planId) throws Exception;

    int deletePlanRelated(String planId) throws Exception;

    int deleteAllPlansRelated() throws Exception;
}
