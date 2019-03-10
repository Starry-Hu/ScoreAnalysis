package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.Plan;
import com.scoreanalysis.dao.PlanMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.PlanService;
import com.scoreanalysis.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project scoreanalysis
 * @ClassName PlanServiceImpl
 * @Author StarryHu
 * @Description 教学计划业务逻辑层
 * @Date 2019/3/5 20:22
 */
@Service("planService")
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanMapper planMapper;

    /** 
    * @Description: 添加教学计划 
    * @Param: [planId, planName, planYear] 
    * @return: int 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    public int addPlan(String planName,String planYear) throws Exception{
        Plan plan = new Plan();
        plan.setPlanId(IDGenerator.generator());
        plan.setPlanName(planName);
        plan.setPlanYear(planYear);
        plan.setIsdel(0);
        
        try {
            int n = planMapper.insert(plan);
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.PLAN_ADD_FAIL);
        }catch (Exception e){
            throw e;
        }
    };

    /** 
    * @Description: 删除教学计划 
    * @Param: [planId] 
    * @return: int 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    public int deletePlan(String planId) throws Exception{
        Plan plan = new Plan();
        plan.setPlanId(planId);
        plan.setIsdel(1);
        
        try{
            int n = planMapper.updateByPrimaryKeySelective(plan);
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.PLAN_DELETE_FAIL);
        }catch (Exception e){
            throw e;
        }
    };

    /** 
    * @Description: 更新教学计划
    * @Param: [planId, planName, planYear] 
    * @return: int 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    public int updatePlan(String planId,String planName,String planYear) throws Exception{
        Plan plan = new Plan();
        plan.setPlanId(planId);
        plan.setPlanName(planName);
        plan.setPlanYear(planYear);
        
        try {
            int n = planMapper.updateByPrimaryKeySelective(plan);
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.PLAN_UPDATE_FAIL);
        }catch (Exception e){
            throw e;
        }
    };
}
