package com.scoreanalysis.pojo;

import com.scoreanalysis.bean.*;

/**
 * @Project scoreanalysis
 * @ClassName StudentExtend
 * @Author StarryHu
 * @Description 学生扩充版pojo
 * @Date 2019/3/5 16:58
 */
public class StudentExtend {
    // 学生对象
    private Student student;

    // 班级(对象)
    private StuClass stuClass;

    // 专业(对象)
    private Major major;

    // 课程-学生对应关系(一个对象)
    private StuCourse stuCourse;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StuClass getStuClass() {
        return stuClass;
    }

    public void setStuClass(StuClass stuClass) {
        this.stuClass = stuClass;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public StuCourse getStuCourse() {
        return stuCourse;
    }

    public void setStuCourse(StuCourse stuCourse) {
        this.stuCourse = stuCourse;
    }

    @Override
    public String toString() {
        return "StudentExtend{" +
                "student=" + student +
                ", stuClass=" + stuClass +
                ", major=" + major +
                ", stuCourse=" + stuCourse +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentExtend that = (StudentExtend) o;

        if (student != null ? !student.equals(that.student) : that.student != null) return false;
        if (stuClass != null ? !stuClass.equals(that.stuClass) : that.stuClass != null) return false;
        if (major != null ? !major.equals(that.major) : that.major != null) return false;
        return stuCourse != null ? stuCourse.equals(that.stuCourse) : that.stuCourse == null;
    }

    @Override
    public int hashCode() {
        int result = student != null ? student.hashCode() : 0;
        result = 31 * result + (stuClass != null ? stuClass.hashCode() : 0);
        result = 31 * result + (major != null ? major.hashCode() : 0);
        result = 31 * result + (stuCourse != null ? stuCourse.hashCode() : 0);
        return result;
    }
}
