package com.scoreanalysis.daoExtend;

import com.scoreanalysis.pojo.StuInfoExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StuInfoExtendMapper {
    // 根据学生id获取学生信息扩展对象的基本信息（除未修课程）
    StuInfoExtend getStuBasicInfoBySid(String sid);

    // 根据学生班级获取全部学生信息扩展对象的基本信息（除未修课程）
    List<StuInfoExtend> getAllStusBasicInfoByClsId(String clsid);

    // 根据专业获取全部学生信息扩展对象的基本信息（除未修课程）
    List<StuInfoExtend> getAllStusBasicInfoByMajor(String majorId);

    // 获取全部学生的信息扩展对象的基本信息（除未修课程）
    List<StuInfoExtend> getAllStusBasicInfo();

    // ---分页相关---
    // 根据班级获取全部学生信息扩展对象的基本信息（除未修课程）
    List<StuInfoExtend> getAllStusBasicInfoByClsIdWithPage(@Param("clsid") String clsid,
                                                           @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    // 根据专业获取全部学生信息扩展对象的基本信息（除未修课程）
    List<StuInfoExtend> getAllStusBasicInfoByMajorWithPage(@Param("majorId") String majorId,
                                                           @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    // 获取全部学生的信息扩展对象的基本信息（除未修课程）
    List<StuInfoExtend> getAllStusBasicInfoWithPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
}
