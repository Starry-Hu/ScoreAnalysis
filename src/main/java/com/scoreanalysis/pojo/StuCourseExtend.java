package com.scoreanalysis.pojo;

/**
 * @Project scoreanalysis
 * @ClassName StuCourseExtend
 * @Author StarryHu
 * @Description 学生课程对应关系扩展类（用于记录课程学分属性）
 * @Date 2019/3/23 16:19
 */
public class StuCourseExtend {
    // 课程号
    private String cid;
    // 课程名称
    private String cname;
    // 课程成绩(在非必修课程里score用来记录这类课程已修的学分数)
    private double score;
    // 课程学分
    private double credit;
    // 课程属性
    private int kcsx;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public int getKcsx() {
        return kcsx;
    }

    public void setKcsx(int kcsx) {
        this.kcsx = kcsx;
    }
}
