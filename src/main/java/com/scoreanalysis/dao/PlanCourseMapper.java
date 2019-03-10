package com.scoreanalysis.dao;

import com.scoreanalysis.bean.PlanCourse;
import com.scoreanalysis.bean.PlanCourseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PlanCourseMapper {
    long countByExample(PlanCourseExample example);

    int deleteByExample(PlanCourseExample example);

    int deleteByPrimaryKey(String pcid);

    int insert(PlanCourse record);

    int insertSelective(PlanCourse record);

    List<PlanCourse> selectByExample(PlanCourseExample example);

    PlanCourse selectByPrimaryKey(String pcid);

    int updateByExampleSelective(@Param("record") PlanCourse record, @Param("example") PlanCourseExample example);

    int updateByExample(@Param("record") PlanCourse record, @Param("example") PlanCourseExample example);

    int updateByPrimaryKeySelective(PlanCourse record);

    int updateByPrimaryKey(PlanCourse record);
}