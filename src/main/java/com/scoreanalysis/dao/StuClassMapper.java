package com.scoreanalysis.dao;

import com.scoreanalysis.bean.StuClass;
import com.scoreanalysis.bean.StuClassExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StuClassMapper {
    long countByExample(StuClassExample example);

    int deleteByExample(StuClassExample example);

    int deleteByPrimaryKey(String clsid);

    int insert(StuClass record);

    int insertSelective(StuClass record);

    List<StuClass> selectByExample(StuClassExample example);

    StuClass selectByPrimaryKey(String clsid);

    int updateByExampleSelective(@Param("record") StuClass record, @Param("example") StuClassExample example);

    int updateByExample(@Param("record") StuClass record, @Param("example") StuClassExample example);

    int updateByPrimaryKeySelective(StuClass record);

    int updateByPrimaryKey(StuClass record);
}