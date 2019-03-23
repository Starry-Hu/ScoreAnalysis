package com.scoreanalysis.pojo;

import com.scoreanalysis.bean.Course;

import java.util.List;

/**
 * @Project scoreanalysis
 * @ClassName StuInfoExtend
 * @Author StarryHu
 * @Description 用于筛选学生课程信息的扩展学生类
 * @Date 2019/3/23 15:41
 */
public class StuInfoExtend {
    // 学号
    private String sid;

    // 学生姓名
    private String sname;

    // 必修：0；限选：1；公选：2
    // 学生已修的必修课程信息(包含分数等)
    private List<StuCourseExtend> doneCourses0;
    // 学生已修的限选课程信息(包含分数等)
    private List<StuCourseExtend> doneCourses1;
    // 学生已修的公选选课程信息(包含分数等)
    private List<StuCourseExtend> doneCourses2;

    // 学生对应教学计划应修的课程信息
    private List<Course> mustCourses;

    // 学生未修课程的展示
    private List<StuCourseExtend> undoCourse;


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public List<Course> getMustCourses() {
        return mustCourses;
    }

    public void setMustCourses(List<Course> mustCourses) {
        this.mustCourses = mustCourses;
    }

    public List<StuCourseExtend> getUndoCourse() {
        return undoCourse;
    }

    public void setUndoCourse(List<StuCourseExtend> undoCourse) {
        this.undoCourse = undoCourse;
    }

    public List<StuCourseExtend> getDoneCourses0() {
        return doneCourses0;
    }

    public void setDoneCourses0(List<StuCourseExtend> doneCourses0) {
        this.doneCourses0 = doneCourses0;
    }

    public List<StuCourseExtend> getDoneCourses1() {
        return doneCourses1;
    }

    public void setDoneCourses1(List<StuCourseExtend> doneCourses1) {
        this.doneCourses1 = doneCourses1;
    }

    public List<StuCourseExtend> getDoneCourses2() {
        return doneCourses2;
    }

    public void setDoneCourses2(List<StuCourseExtend> doneCourses2) {
        this.doneCourses2 = doneCourses2;
    }
}
