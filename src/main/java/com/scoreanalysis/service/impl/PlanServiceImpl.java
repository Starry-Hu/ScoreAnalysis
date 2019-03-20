package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.Course;
import com.scoreanalysis.bean.Plan;
import com.scoreanalysis.bean.PlanCourse;
import com.scoreanalysis.bean.PlanCourseExample;
import com.scoreanalysis.dao.CourseMapper;
import com.scoreanalysis.dao.PlanCourseMapper;
import com.scoreanalysis.dao.PlanMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.PlanService;
import com.scoreanalysis.util.IDGenerator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
     * @Description: 添加教学计划
     * @Param: [planId, planName, planYear]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/5
     */
    public Plan addPlan(String planName, String planYear) throws Exception {
        Plan plan = new Plan();
        plan.setPlanId(IDGenerator.generator());
        plan.setPlanName(planName);
        plan.setPlanYear(planYear);

        try {
            int n = planMapper.insertSelective(plan);
            if (n > 0) {
                return plan;
            }

            throw new SAException(ExceptionEnum.PLAN_ADD_FAIL);

        } catch (Exception e) {
            throw e;
        }
    }

    ;

    /**
     * @Description: 删除教学计划
     * @Param: [planId]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/5
     */
    public int deletePlan(String planId) throws Exception {
        try {
            int n = planMapper.deleteByPrimaryKey(planId);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.PLAN_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 更新教学计划
     * @Param: [planId, planName, planYear]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/5
     */
    public int updatePlan(String planId, String planName, String planYear) throws Exception {
        Plan plan = new Plan();
        plan.setPlanId(planId);
        plan.setPlanName(planName);
        plan.setPlanYear(planYear);

        try {
            int n = planMapper.updateByPrimaryKeySelective(plan);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.PLAN_UPDATE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }



    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean batchUpload(String fileName, MultipartFile file, boolean isExcel2003, String planId) throws Exception {
        boolean notNull = false;
        List<Course> courseList = new ArrayList<>();

        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            notNull = true;
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

            if (row.getCell(1).getCellType() != 1) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程号请设为文本格式)");
            }
            String cname = row.getCell(1).getStringCellValue();

            if (cname == null || cname.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程名未填写)");
            }

            double credit = row.getCell(2).getNumericCellValue();
//          double credit = Double.parseDouble(creditString);
            if (credit == 0) {
                throw new Exception("导入失败(第" + (r + 1) + "行,请正确填写学分)");
            }

            String isAcquired = row.getCell(3).getStringCellValue();
            if (isAcquired == null || isAcquired.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,是否必修未填写)");
            }
            // 是必修
            if (isAcquired.equals("y")) {
                course.setIsAcquired(1);
            } else {
                course.setIsAcquired(0);
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
//                System.out.println(" 插入 " + courseRecord);
            } else if (testList.size() == 0){
                // 课程存在-更新；关系不存在-添加
                courseMapper.updateByPrimaryKeySelective(courseRecord);
                planCourseMapper.insert(planCourse);
//                System.out.println(" 更新 " + courseRecord);
            }else{
                // 都存在时，只需要更新原课程信息即可。关系对象是不变的（存在）
                courseMapper.insert(courseRecord);
            }
        }

        return notNull;
    }

    /**
     * @Description: 根据教学计划删除所有相关信息(plan，planCourse)
     * @Param: [planId, cid]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    public int deletePlanRelated(String planId) throws Exception {
        PlanCourseExample example = new PlanCourseExample();
        PlanCourseExample.Criteria criteria = example.createCriteria();
        criteria.andPlanIdEqualTo(planId);

        try {
            // 删除教学计划课程关系  和   教学计划
            int n1 = planCourseMapper.deleteByExample(example);
            int n2 = planMapper.deleteByPrimaryKey(planId);
            if (n1 > 0 && n2 > 0) {
                return n1+n2;
            }
            throw new SAException(ExceptionEnum.PLAN_COURSE_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }
}
