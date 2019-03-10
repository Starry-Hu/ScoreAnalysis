package com.scoreanalysis.service;

public interface PlanCourseService {
    int addPlanCourse(String planId,String cid);

    int deletePlanCourse(String planId,String cid);

    int updatePlanCourse(String planId,String cid);
}
