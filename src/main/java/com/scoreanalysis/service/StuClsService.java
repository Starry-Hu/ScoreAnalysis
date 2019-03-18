package com.scoreanalysis.service;

public interface StuClsService {
    int addStuCls(String clsId,String clsName,String clsMajor) throws Exception;

    int deleteStuCls(String clsId) throws Exception;

    int updateStuCls(String clsId,String clsName,String clsMajor) throws Exception;
}
