package com.scoreanalysis.bean;

public class Course {
    private String cid;

    private String cname;

    private Double credit;

    private Integer kcsx;

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

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Integer getKcsx() {
        return kcsx;
    }

    public void setKcsx(Integer kcsx) {
        this.kcsx = kcsx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (cid != null ? !cid.equals(course.cid) : course.cid != null) return false;
        if (cname != null ? !cname.equals(course.cname) : course.cname != null) return false;
        if (credit != null ? !credit.equals(course.credit) : course.credit != null) return false;
        return kcsx != null ? kcsx.equals(course.kcsx) : course.kcsx == null;
    }

    @Override
    public int hashCode() {
        int result = cid != null ? cid.hashCode() : 0;
        result = 31 * result + (cname != null ? cname.hashCode() : 0);
        result = 31 * result + (credit != null ? credit.hashCode() : 0);
        result = 31 * result + (kcsx != null ? kcsx.hashCode() : 0);
        return result;
    }
}