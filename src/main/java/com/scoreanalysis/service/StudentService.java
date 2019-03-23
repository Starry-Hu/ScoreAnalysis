package com.scoreanalysis.service;

import com.scoreanalysis.pojo.StuCourseExtend;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    // 学生相关

    // 学生课程相关

    // 上传学生信息excel
    void batchUpload(MultipartFile file,String planId) throws Exception;
    // 删除全部学生相关信息
    int deleteAllStusRelated() throws Exception;
    // 获取某学生的修课情况
    List<StuCourseExtend> getStuInfoBySid(String sid) throws Exception;
}
