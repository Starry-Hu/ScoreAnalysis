package com.scoreanalysis.service;

import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    // 学生相关

    // 学生课程相关

    // 上传学生信息excel
    void batchUpload(MultipartFile file) throws Exception;

    int deleteAllStusRelated() throws Exception;
}
