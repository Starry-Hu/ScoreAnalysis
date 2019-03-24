package com.scoreanalysis.bean;

public class StuCourse {
    private String scid;

    private String sid;

    private String cid;

    private Double score;

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StuCourse stuCourse = (StuCourse) o;

//        if (scid != null ? !scid.equals(stuCourse.scid) : stuCourse.scid != null) return false;
        if (sid != null ? !sid.equals(stuCourse.sid) : stuCourse.sid != null) return false;
        if (cid != null ? !cid.equals(stuCourse.cid) : stuCourse.cid != null) return false;
        return score != null ? score.equals(stuCourse.score) : stuCourse.score == null;
    }

    @Override
    public int hashCode() {
        int result = scid != null ? scid.hashCode() : 0;
        result = 31 * result + (sid != null ? sid.hashCode() : 0);
        result = 31 * result + (cid != null ? cid.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}