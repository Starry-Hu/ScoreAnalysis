package com.scoreanalysis.service;

import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    // 学生相关
    int addStudent(String sid,String sname,String sclass,String smajor) throws Exception;

    int deleteStudentBySid(String sid) throws Exception;

    int deleteAllStus() throws Exception;

    int updateStudent(String sid,String sname,String sclass,String smajor) throws Exception;

    // 学生课程相关
    int addStuCourse(String scid,String sid,String cid,int score) throws Exception;

    int deleteStuCourse(String sid) throws Exception;

    int updateStuCourse(String scid,String sid,String cid,int score) throws Exception;

    // 上传学生信息excel
    boolean batchUpload(String fileName, MultipartFile file,boolean isExcel2003) throws Exception;
}
