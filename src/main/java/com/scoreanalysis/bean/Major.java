package com.scoreanalysis.bean;

public class Major {
    private String mid;

    private String mname;

    private String mplan;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMplan() {
        return mplan;
    }

    public void setMplan(String mplan) {
        this.mplan = mplan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Major major = (Major) o;

        if (mid != null ? !mid.equals(major.mid) : major.mid != null) return false;
        if (mname != null ? !mname.equals(major.mname) : major.mname != null) return false;
        return mplan != null ? mplan.equals(major.mplan) : major.mplan == null;
    }

    @Override
    public int hashCode() {
        int result = mid != null ? mid.hashCode() : 0;
        result = 31 * result + (mname != null ? mname.hashCode() : 0);
        result = 31 * result + (mplan != null ? mplan.hashCode() : 0);
        return result;
    }
}