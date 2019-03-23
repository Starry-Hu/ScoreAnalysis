package com.scoreanalysis.daoExtend;

import com.scoreanalysis.bean.Course;
import com.scoreanalysis.pojo.StuCourseExtend;

import java.util.List;

public interface CourseExtendMapper {
    // 获取教学计划里应修的课程数组
    List<Course> getMustCoursInPlan(String planId);

    // 获取某学生已修的全部必修课程
    List<StuCourseExtend> getDoneCoursAcquBySid(String sid);

    // 获取某学生的非必修的某类全部课程（1限选2公选）
    List<StuCourseExtend> getDoneCoursNoAcquByKcsx(Integer kcsx);
}
