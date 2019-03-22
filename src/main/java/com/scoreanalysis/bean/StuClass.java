package com.scoreanalysis.bean;

public class StuClass {
    private String clsid;

    private String clsName;

    private String clsMajor;

    public String getClsid() {
        return clsid;
    }

    public void setClsid(String clsid) {
        this.clsid = clsid;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getClsMajor() {
        return clsMajor;
    }

    public void setClsMajor(String clsMajor) {
        this.clsMajor = clsMajor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StuClass stuClass = (StuClass) o;

        if (clsid != null ? !clsid.equals(stuClass.clsid) : stuClass.clsid != null) return false;
        if (clsName != null ? !clsName.equals(stuClass.clsName) : stuClass.clsName != null) return false;
        return clsMajor != null ? clsMajor.equals(stuClass.clsMajor) : stuClass.clsMajor == null;
    }

    @Override
    public int hashCode() {
        int result = clsid != null ? clsid.hashCode() : 0;
        result = 31 * result + (clsName != null ? clsName.hashCode() : 0);
        result = 31 * result + (clsMajor != null ? clsMajor.hashCode() : 0);
        return result;
    }
}