package com.scoreanalysis.service;

import com.scoreanalysis.pojo.StuInfoExtend;
import com.scoreanalysis.util.PageBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    // 上传学生信息excel
    void batchUpload(MultipartFile file, String planId) throws Exception;

    // ---分页相关---
    // 分页获取某个学生的修课情况（带分页/主要是统一返回pagebean）
    PageBean<StuInfoExtend> getStuInfoBySid(String sid, int pageNum, int pageSize) throws Exception;

    // 根据班级获取该班级全部学生的相关修课信息(带分页)
    PageBean<StuInfoExtend> getStuInfoByStuClsWithPage(String clsId, int pageNum, int pageSize) throws Exception;

    // 根据专业获取该专业全部学生的相关修课信息(带分页)
    PageBean<StuInfoExtend> getStuInfoByMajorWithPage(String majorId, int pageNum, int pageSize) throws Exception;

    // 获取整个年级（即全部）全部学生的修课情况(带分页)
    PageBean<StuInfoExtend> getAllStuInfoWithPage(int pageNum, int pageSize) throws Exception;

    // --- 单个功能 未与分页相关联  暂时搁置---
    // 获取某学生的修课情况
    StuInfoExtend getStuInfoBySid(String sid) throws Exception;

    // 获取某个班级的全部学生的修课情况
    List<StuInfoExtend> getStuInfoByStuCls(String clsId) throws Exception;

    // 获取某个专业的全部学生的修课情况
    List<StuInfoExtend> getStuInfoByMajor(String majorId) throws Exception;

    // 获取整个年级（即全部）全部学生的修课情况
    List<StuInfoExtend> getAllStuInfo() throws Exception;


}
