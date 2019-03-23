package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.*;
import com.scoreanalysis.dao.CourseMapper;
import com.scoreanalysis.dao.PlanCourseMapper;
import com.scoreanalysis.dao.PlanMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.PlanService;
import com.scoreanalysis.util.IDGenerator;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private PlanCourseMapper planCourseMapper;

    /**
     * @Description: 上传教学计划文件，将数据导入plan表和plan_course表
     * @Param: [file, planId]
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/22
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void batchUpload(MultipartFile file, String planName) throws Exception {
        // 添加教学计划基本信息
        Plan plan;
        String planId;
        PlanExample planExample = new PlanExample();
        planExample.createCriteria().andPlanNameEqualTo(planName);
        List<Plan> planList = planMapper.selectByExample(planExample);
        // 检测同名教学计划是否存在;同名教学计划已存在,则使用该教学计划的id为基准
        // 不存在同名，则插入新的教学计划
        if (planList.size() == 1) {
            planId = planList.get(0).getPlanId();
        } else {
            plan = new Plan();
            plan.setPlanId(IDGenerator.generator());
            plan.setPlanName(planName);
            planId = plan.getPlanId();

            int n = planMapper.insertSelective(plan);
            if (n <= 0) {
                throw new SAException(ExceptionEnum.PLAN_ADD_FAIL);
            }
        }


        // 导入excel到相关表中
        List<Course> courseList = new ArrayList<>();

        InputStream is = file.getInputStream();
        // 使用spring自带的兼容2003/2007
        Workbook wb = WorkbookFactory.create(is);

        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null) {
            throw new SAException(ExceptionEnum.UPLOAD_EMPTY);
        }

        Course course;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            //sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException
            //r = 2 表示从第三行开始循环 如果你的第三行开始是数据
            Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
            if (row == null) {
                continue;
            }

            // 判断每个单元格的值，同时生成对象
            course = new Course();

            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String cid = row.getCell(0).getStringCellValue();
            if (cid == null || cid.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程号未填写)");
            }

            String cname = row.getCell(1).getStringCellValue();
            if (cname == null || cname.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程名未填写)");
            }

            row.getCell(2).setCellType(Cell.CELL_TYPE_NUMERIC);
            double credit = row.getCell(2).getNumericCellValue();
            if (credit == 0) {
                throw new Exception("导入失败(第" + (r + 1) + "行,请正确填写学分)");
            }

            String kcsx = row.getCell(3).getStringCellValue();
            if (kcsx == null || kcsx.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程属性未填写)");
            }
            // 是必修
            if (kcsx.equals("必修")) {
                course.setKcsx(0);
            } else if (kcsx.equals("限选")) {
                course.setKcsx(1);
            } else if (kcsx.equals("公选")) {
                course.setKcsx(2);
            } else {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程属性填写不正确)");
            }

            //完整的循环一次 就组成了一个对象
            course.setCid(cid);
            course.setCname(cname);
            course.setCredit(credit);
            courseList.add(course);

        }

        // 插入或更新课程列表
        for (Course courseRecord : courseList) {
            // 获取相同课程号的课程
            String cid = courseRecord.getCid();
            Course testCourse = courseMapper.selectByPrimaryKey(cid);

            PlanCourseExample example = new PlanCourseExample();
            PlanCourseExample.Criteria criteria = example.createCriteria();
            criteria.andPlanIdEqualTo(planId);
            criteria.andCidEqualTo(cid);
            // 获取相同的planid和cid的教学计划_课程关系
            List<PlanCourse> testList = planCourseMapper.selectByExample(example);

            // 建立计划_课程关系对象
            String pcid = IDGenerator.generator();
            PlanCourse planCourse = new PlanCourse();
            planCourse.setPcid(pcid);
            planCourse.setPlanId(planId);
            planCourse.setCid(cid);

            // 分情况讨论不同的处理方式
            if (testCourse == null) {
                // 课程不存在时,课程和关系都添加
                courseMapper.insert(courseRecord);
                planCourseMapper.insert(planCourse);
            } else if (testList.size() == 0) {
                // 课程存在-若对比发现不同则更新，否则不管；关系不存在-添加
                if (!testCourse.equals(courseRecord)) {
                    courseMapper.updateByPrimaryKeySelective(courseRecord);
                }
                planCourseMapper.insert(planCourse);
            } else {
                // 都存在时，只需要对比发现是否需要更新原课程信息即可。
                // 关系对象是不变的（存在）
                if (!testCourse.equals(courseRecord)) {
                    courseMapper.updateByPrimaryKeySelective(courseRecord);
                }
            }
        }
    }

    /**
     * @Description: 根据教学计划id删除所有相关信息(plan ， planCourse)
     * @Param: [planId]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    public int deletePlanRelated(String planId) throws Exception {
        PlanCourseExample example = new PlanCourseExample();
        PlanCourseExample.Criteria criteria = example.createCriteria();
        criteria.andPlanIdEqualTo(planId);

        try {
            Plan testPlan = planMapper.selectByPrimaryKey(planId);
            List<PlanCourse> planCourseList = planCourseMapper.selectByExample(example);
            if (testPlan == null || planCourseList.size() == 0) {
                throw new SAException(ExceptionEnum.PLAN_DATA_EMPTY);
            }

            // 删除教学计划课程关系  和   教学计划
            int n1 = planCourseMapper.deleteByExample(example);
            int n2 = planMapper.deleteByPrimaryKey(planId);
            if (n1 > 0 && n2 > 0) {
                return n1 + n2;
            }
            throw new SAException(ExceptionEnum.PLAN_DATA_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 删除全部教学计划相关
     * @Param: []
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/22
     */
    public int deleteAllPlansRelated() throws Exception {
        // 查出全部的教学计划-课程关系
        PlanCourseExample planCourseExample = new PlanCourseExample();
        planCourseExample.createCriteria().andPcidIsNotNull();

        // 查出全部的教学计划
        PlanExample planExample = new PlanExample();
        planExample.createCriteria().andPlanIdIsNotNull();

        List<Plan> planList = planMapper.selectByExample(planExample);
        List<PlanCourse> planCourseList = planCourseMapper.selectByExample(planCourseExample);

        if (planList.size() == 0 && planCourseList.size() == 0) {
            throw new SAException(ExceptionEnum.PLAN_DATA_EMPTY);
        }
        int n1 = planMapper.deleteByExample(planExample);
        int n2 = planCourseMapper.deleteByExample(planCourseExample);

        if (n1 <= 0 && n2 <= 0) {
            throw new SAException(ExceptionEnum.PLAN_DATA_DELETE_FAIL);
        } else {
            return n1 + n2;
        }
    }

    /**
    * @Description: 获得全部教学计划基本信息对象
    * @Param: []
    * @return: java.util.List<com.scoreanalysis.bean.Plan>
    * @Author: StarryHu
    * @Date: 2019/3/23
    */
    public List<Plan> getAllPlans() throws Exception {
        PlanExample planExample = new PlanExample();
        planExample.createCriteria().andPlanIdIsNotNull();
        List<Plan> planList = planMapper.selectByExample(planExample);
        if (planList.size() == 0){
            throw new SAException(ExceptionEnum.PLAN_NO_EXIST);
        }
        return planList;
    }
}
