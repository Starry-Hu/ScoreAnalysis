package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.Course;
import com.scoreanalysis.bean.CourseExample;
import com.scoreanalysis.bean.Plan;
import com.scoreanalysis.bean.PlanExample;
import com.scoreanalysis.dao.CourseMapper;
import com.scoreanalysis.dao.PlanMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.PlanService;
import com.scoreanalysis.util.ExcelImportUtil;
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

    /**
     * @Description: 上传教学计划文件，添加基本信息入plan表，并将数据导入course表和plan_course表
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

            // 1.到终点了则退出循环
            // 2.解决POI读取最大行数时将带格式空行读取或存在空行，导致批量导入失败的问题
            if (row == null || ExcelImportUtil.isRowEmpty(row)) {
                continue;
            }

            course = new Course();

            // 班级ID
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
            // 分情况赋各种课程属性值，并设置非必修课程的通用课程号
            if (kcsx.equals("必修")) {
                course.setKcsx(0);
            } else if (kcsx.equals("限选")) {
                cid = "88888888x";
                cname = "限选";
                course.setKcsx(1);
            } else if (kcsx.equals("任选")) {
                cid = "99999999x";
                cname = "任选";
                course.setKcsx(2);
            } else {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程属性填写不正确)");
            }

            //完整的循环一次 就组成了一个对象
            course.setUucid(IDGenerator.generator());
            course.setCid(cid);
            course.setCname(cname);
            course.setCredit(credit);
            course.setPlanId(planId);
            courseList.add(course);
        }

        // 插入或更新课程列表
        for (Course courseRecord : courseList) {
            // 获取相同课程号的课程
            CourseExample courseExample = new CourseExample();
            courseExample.createCriteria().andCidEqualTo(courseRecord.getCid());
            List<Course> testCourlist = courseMapper.selectByExample(courseExample);

            // 分情况讨论该课程号对应的课程是否存在；执行不同的处理方式
            if (testCourlist.size() == 0) {
                // 不存在时则添加
                courseMapper.insert(courseRecord);
            } else {
                // 记录是否教学计划不同
                boolean planSame = false;
                // 遍历相同课程号的课程数组
                for (Course testCourse:testCourlist) {
                    // 如果存在某个课程与现在课程完全相同（除uucid），则退出循环
                    if (testCourse.equals(courseRecord)) {
                        planSame = true;
                        break;
                    } else{
                        // 课程存在:若对比发现不同则判断是否是教学计划不同(目的是找教学计划相同的那个课程)
                        // 如果两者的教学计划不同,则将新的课程对象插入;（全部遍历完之后没有找到相同教学计划的那个课程）
                        // 而如果教学计划相同,则是课程对象自身信息不同;由于uuid所以将原来的删掉再添加新的,并退出循环
                        if (testCourse.getPlanId().equals(courseRecord.getPlanId())) {
                            planSame = true;
                            courseMapper.deleteByPrimaryKey(testCourse.getUucid());
                            courseMapper.insert(courseRecord);
                            break;
                        }
                    }
                }

                // 如果遍历完发现存在课程号相同的但是教学计划不同，则插入该新数据
                if (!planSame){
                    courseMapper.insert(courseRecord);
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
        // 查出所有的课程信息
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria().andCidIsNotNull();

        // 查找教学计划、全部课程
        Plan testPlan = planMapper.selectByPrimaryKey(planId);
        List<Course> courseList = courseMapper.selectByExample(courseExample);
        if (testPlan == null || courseList.size() == 0) {
            throw new SAException(ExceptionEnum.PLAN_DATA_EMPTY);
        }

        // 删除教学计划,课程和教学计划课程关系
        int n1 = courseMapper.deleteByExample(courseExample);
        int n2 = planMapper.deleteByPrimaryKey(planId);

        if (n1 <= 0 || n2 <= 0) {
            throw new SAException(ExceptionEnum.PLAN_DATA_DELETE_FAIL);
        }
        return n1 + n2;
    }

    /**
     * @Description: 删除全部教学计划相关
     * @Param: []
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/22
     */
    public int deleteAllPlansRelated() throws Exception {
        // 查出全部的教学计划
        PlanExample planExample = new PlanExample();
        planExample.createCriteria().andPlanIdIsNotNull();

        // 查出全部的课程
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria().andCidIsNotNull();

        List<Plan> planList = planMapper.selectByExample(planExample);
        List<Course> courseList = courseMapper.selectByExample(courseExample);

        if (planList.size() == 0 || courseList.size() == 0) {
            throw new SAException(ExceptionEnum.PLAN_DATA_EMPTY);
        }
        int n1 = planMapper.deleteByExample(planExample);
        int n2 = courseMapper.deleteByExample(courseExample);

        if (n1 <= 0 || n2 <= 0) {
            throw new SAException(ExceptionEnum.PLAN_DATA_DELETE_FAIL);
        }
        return n1 + n2;
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
        if (planList.size() == 0) {
            throw new SAException(ExceptionEnum.PLAN_NO_EXIST);
        }
        return planList;
    }
}
