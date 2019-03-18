package com.scoreanalysis.bean;

public class Course {
    private String cid;

    private String cname;

    private Double credit;

    private Integer isAcquired;

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

    public Integer getIsAcquired() {
        return isAcquired;
    }

    public void setIsAcquired(Integer isAcquired) {
        this.isAcquired = isAcquired;
    }
}