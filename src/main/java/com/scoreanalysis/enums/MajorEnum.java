package com.scoreanalysis.enums;

/**
 * @Project scoreanalysis
 * @ClassName MajorEnum
 * @Author StarryHu
 * @Description 专业枚举
 * @Date 2019/3/22 9:08
 */
public enum MajorEnum {
    JIKE("01","计"),
    RUANJIAN("02","软件"),
    WANGLUO("03","网络"),
    XINAN("04","信安");


    private String mid;

    private String mname;

    MajorEnum(String mid, String mname){
        this.mid = mid;
        this.mname = mname;
    }

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
}
