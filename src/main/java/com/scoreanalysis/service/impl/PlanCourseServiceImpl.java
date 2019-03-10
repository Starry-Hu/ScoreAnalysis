package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.PlanCourse;
import com.scoreanalysis.dao.PlanCourseMapper;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.PlanCourseService;
import com.scoreanalysis.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project scoreanalysis
 * @ClassName PlanCourseServiceImpl
 * @Author StarryHu
 * @Description 课程教学计划 对应关系逻辑层
 * @Date 2019/3/5 19:41
 */
@Service("planCourseService")
public class PlanCourseServiceImpl implements PlanCourseService {
    @Autowired
    private PlanCourseMapper planCourseMapper;

    public int addPlanCourse(String planId, String cid) {
        PlanCourse planCourse = new PlanCourse();
        // 生成32位uuid
        planCourse.setPcid(IDGenerator.generator());
        planCourse.setPlanId(planId);
        planCourse.setCid(cid);
        try {
            int n = planCourseMapper.insert(planCourse);
            if (n > 0) {
                return n;
            }
        } catch (Exception e) {
            throw e;
        }

    };

    public int deletePlanCourse(String planId, String cid){

    };

    public int updatePlanCourse(String planId, String cid){

    };
}
