package com.scoreanalysis.dao;

import com.scoreanalysis.bean.Plan;
import com.scoreanalysis.bean.PlanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PlanMapper {
    long countByExample(PlanExample example);

    int deleteByExample(PlanExample example);

    int deleteByPrimaryKey(String planId);

    int insert(Plan record);

    int insertSelective(Plan record);

    List<Plan> selectByExample(PlanExample example);

    Plan selectByPrimaryKey(String planId);

    int updateByExampleSelective(@Param("record") Plan record, @Param("example") PlanExample example);

    int updateByExample(@Param("record") Plan record, @Param("example") PlanExample example);

    int updateByPrimaryKeySelective(Plan record);

    int updateByPrimaryKey(Plan record);
}