package com.scoreanalysis.daoExtend;

import com.scoreanalysis.pojo.StuInfoExtend;

import java.util.List;

public interface StuInfoExtendMapper {
    // 根据学生id获取学生信息扩展对象的基本信息（除未修课程）
    StuInfoExtend getStuBasicInfoBySid(String sid);
    // 根据学生班级获取全部学生信息扩展对象的基本信息（除未修课程）
    List<StuInfoExtend> getAllStusBasicInfoByClsId(String clsid);
    // 根据专业获取全部学生信息扩展对象的基本信息（除未修课程）
    List<StuInfoExtend> getAllStusBasicInfoByMajor(String majorId);
}
