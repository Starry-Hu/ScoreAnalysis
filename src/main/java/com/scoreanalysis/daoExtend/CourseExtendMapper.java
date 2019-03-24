package com.scoreanalysis.daoExtend;

import com.scoreanalysis.bean.Course;
import com.scoreanalysis.pojo.StuCourseExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseExtendMapper {
    // 获取教学计划里应修的课程数组
    List<Course> getMustCoursInPlan(String planId);

    // 获取某学生已修的某类课程（0必修1限选2公选）
    List<StuCourseExtend> getDoneCoursBySid(@Param("sid") String sid, @Param("kcsx") Integer kcsx, @Param("planId") String planId);
}
