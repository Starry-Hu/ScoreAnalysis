package com.scoreanalysis.dao;

import com.scoreanalysis.bean.StuCourse;
import com.scoreanalysis.bean.StuCourseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StuCourseMapper {
    long countByExample(StuCourseExample example);

    int deleteByExample(StuCourseExample example);

    int deleteByPrimaryKey(String scid);

    int insert(StuCourse record);

    int insertSelective(StuCourse record);

    List<StuCourse> selectByExample(StuCourseExample example);

    StuCourse selectByPrimaryKey(String scid);

    int updateByExampleSelective(@Param("record") StuCourse record, @Param("example") StuCourseExample example);

    int updateByExample(@Param("record") StuCourse record, @Param("example") StuCourseExample example);

    int updateByPrimaryKeySelective(StuCourse record);

    int updateByPrimaryKey(StuCourse record);
}