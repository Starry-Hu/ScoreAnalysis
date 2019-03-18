package com.scoreanalysis.service;

import com.scoreanalysis.bean.Course;

public interface CourseService {
    int addCourse(String cid,String cname,double credit) throws Exception;

    int deleteCourse(String cid) throws Exception;

    int updateCourse(String cid,String cname,double credit);

    Course getCourseById(String cid) throws Exception;

    // 为内部使用的查找方法  无异常的抛出
    Course findCourseById(String cid);
}
