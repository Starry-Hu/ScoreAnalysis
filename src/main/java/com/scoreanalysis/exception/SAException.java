package com.scoreanalysis.exception;

import com.scoreanalysis.enums.ExceptionEnum;

/**
 * @ClassName SAException
 * @Author yang_
 * @Description "ScoreAnalysis系统自定义异常"
 * @Date 2018/11/4 18:32
 */
public class SAException extends RuntimeException {

    private Integer code;
    private String msg;

    public SAException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
