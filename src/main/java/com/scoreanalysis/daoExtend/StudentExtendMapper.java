package com.scoreanalysis.daoExtend;

import com.scoreanalysis.pojo.StudentExtend;

import java.util.List;

public interface StudentExtendMapper {
    List<StudentExtend> getStudents();

    String getStudentPlanId(String sId);

}
