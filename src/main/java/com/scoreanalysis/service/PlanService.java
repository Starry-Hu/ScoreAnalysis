package com.scoreanalysis.service;

public interface PlanService {
    int addPlan(String planName,String planYear) throws Exception;

    int deletePlan(String planId) throws Exception;

    int updatePlan(String planId,String planName,String planYear) throws Exception;
}
