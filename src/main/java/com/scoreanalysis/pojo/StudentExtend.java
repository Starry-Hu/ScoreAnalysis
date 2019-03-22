package com.scoreanalysis.pojo;

import com.scoreanalysis.bean.Major;
import com.scoreanalysis.bean.StuClass;
import com.scoreanalysis.bean.Student;

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

    @Override
    public String toString() {
        return "StudentExtend{" +
                "student=" + student +
                ", stuClass=" + stuClass +
                ", major=" + major +
                '}';
    }
}
