package com.scoreanalysis.service;

import com.scoreanalysis.pojo.StuInfoExtend;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    // 上传学生信息excel
    void batchUpload(MultipartFile file,String planId) throws Exception;
    // 删除全部学生相关信息
    int deleteAllStusRelated() throws Exception;
    // 获取某学生的修课情况
    StuInfoExtend getStuInfoBySid(String sid) throws Exception;
    // 获取某个班级的全部学生的修课情况
    List<StuInfoExtend> getStuInfoByStuCls(String clsId) throws Exception;
    // 获取某个专业的全部学生的修课情况
    List<StuInfoExtend> getStuInfoByMajor(String majorId) throws Exception;
}
