package com.scoreanalysis.pojo;

/**
 * @Project scoreanalysis
 * @ClassName StudentExtend
 * @Author StarryHu
 * @Description 学生扩充版pojo
 * @Date 2019/3/5 16:58
 */
public class StudentExtend {

    // 学号
    private String stuId;

    // 学生姓名
    private String stuName;

    // 专业名称
    private String majorName;

    // 班级
    private String className;

    // 已修学分
    private Double nowCredit;

    // 应修学分
    private Double planCredit;

    // 是否修满
    private Boolean isFull;

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Double getNowCredit() {
        return nowCredit;
    }

    public void setNowCredit(Double nowCredit) {
        this.nowCredit = nowCredit;
    }

    public Double getPlanCredit() {
        return planCredit;
    }

    public void setPlanCredit(Double planCredit) {
        this.planCredit = planCredit;
    }

    public Boolean getFull() {
        return isFull;
    }

    public void setFull(Boolean full) {
        isFull = full;
    }

    @Override
    public String toString() {
        return "StudentExtend{" +
                "stuId='" + stuId + '\'' +
                ", stuName='" + stuName + '\'' +
                ", majorName='" + majorName + '\'' +
                ", className='" + className + '\'' +
                ", nowCredit=" + nowCredit +
                ", planCredit=" + planCredit +
                ", isFull=" + isFull +
                '}';
    }
}
