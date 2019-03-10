package com.scoreanalysis.util;

/**
 * @ClassName BaseResponse
 * @Author yang_
 * @Description 所有Controller的返回格式都为该格式
 * @Date 2018/11/4 18:11
 */
public class BaseResponse {

    /** 返回的状态码 */
    private Integer code;

    /** 返回的信息 */
    private String msg;

    /** 返回的数据 */
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
