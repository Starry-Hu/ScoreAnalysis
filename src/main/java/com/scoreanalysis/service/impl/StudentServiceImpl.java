package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.*;
import com.scoreanalysis.dao.*;
import com.scoreanalysis.daoExtend.CourseExtendMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.enums.MajorEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.pojo.StuCourseExtend;
import com.scoreanalysis.pojo.StuInfoExtend;
import com.scoreanalysis.pojo.StudentExtend;
import com.scoreanalysis.service.StudentService;
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
 * @ClassName StudentServiceImpl
 * @Author StarryHu
 * @Description 学生的相关业务逻辑层
 * @Date 2019/3/10 17:14
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private MajorMapper majorMapper;
    @Autowired
    private StuClassMapper stuClassMapper;
    @Autowired
    private StuCourseMapper stuCourseMapper;
    @Autowired
    private CourseExtendMapper courseExtendMapper;

    /**
     * @Description: 上传学生成绩信息相关，将数据导入student表，stuClass表以及major表
     * @Param: [file]
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/22
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void batchUpload(MultipartFile file, String planId) throws Exception {
        List<StudentExtend> studentExtendList = new ArrayList<>();

        InputStream is = file.getInputStream();
        // 使用spring自带的兼容2003/2007
        Workbook wb = WorkbookFactory.create(is);

        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null) {
            throw new SAException(ExceptionEnum.UPLOAD_EMPTY);
        }

        // 扩展类学生对象
        StudentExtend studentExtend;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            //sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException
            //r = 1 表示从第二行开始循环 如果你的第二行开始是数据
            Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
            if (row == null) {
                continue;
            }

            // 判断每个单元格的值，同时生成对象
            studentExtend = new StudentExtend();

            // 学号
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String sid = row.getCell(0).getStringCellValue();
            if (sid == null || sid.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,学号未填写)");
            }


            // 姓名
            String sname = row.getCell(1).getStringCellValue();
            if (sname == null || sname.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,姓名未填写)");
            }

            // 班级
            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
            String stuClassValue = row.getCell(3).getStringCellValue();
            if (stuClassValue == null || stuClassValue.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,班级未填写)");
            }

            // 建立班级对象和专业对象
            StuClass stuClass = new StuClass();
            Major major = new Major();
            for (MajorEnum majorEnum : MajorEnum.values()) {
                if (stuClassValue.contains(majorEnum.getMname())) {
                    // 取专业枚举值填充专业对象
                    major.setMid(majorEnum.getMid());
                    major.setMname(majorEnum.getMname());
                    // 设置该专业对应的教学计划
                    major.setMplan(planId);

                    // 取值填充班级类
                    // 将专业名替代后剩下的部分为班级id,同时并上专业id(e.g:011701/计科1701)
                    stuClass.setClsid(major.getMid() + stuClassValue.replace(majorEnum.getMname(), ""));
                    stuClass.setClsMajor(majorEnum.getMid());
                    stuClass.setClsName(stuClassValue);
                    break;
                }
            }

            // 课程-学生对应关系（一门）

            // 课程号
            row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
            String cid = row.getCell(5).getStringCellValue();
            if (cid == null || cid.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程号未填写)");
            }


            // 课程属性;在课程属性是非必修的时候设置特定通用的课程号;必修则指定为对应的课程属性代号
            String kcsxValue = row.getCell(9).getStringCellValue();
            if (kcsxValue == null || kcsxValue.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程属性未填写)");
            }
            // 分情况赋予非必修特定的课程号
            if (kcsxValue.equals("限选")) {
                cid = "88888888x";
            } else if (kcsxValue.equals("公选")) {
                cid = "99999999x";
            } else {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程属性填写不正确)");
            }

            // 学生该门课程对应的成绩
            row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
            String scoreValue = row.getCell(10).getStringCellValue();
            if (scoreValue == null || scoreValue.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程成绩未填写)");
            }
            scoreValue = scoreValue.replace("-", "");
            double score = Double.parseDouble(scoreValue);

            // 非必修时，将获取该课程的学分信息设为score填充进去
            if (!kcsxValue.equals("必修")) {
                row.getCell(7).setCellType(Cell.CELL_TYPE_NUMERIC);
                double credit = row.getCell(7).getNumericCellValue();
                if (credit == 0) {
                    throw new Exception("导入失败(第" + (r + 1) + "行,非必修课程学分未填写)");
                }
                score = credit;
            }

            // 组成一个课程-学生对应关系对象
            StuCourse stuCourse = new StuCourse();
            stuCourse.setScid(IDGenerator.generator());
            stuCourse.setCid(cid);
            stuCourse.setSid(sid);
            stuCourse.setScore(score);

            // 完整的循环一次 就组成了一个对象
            // 生成学生对象
            Student student = new Student();
            student.setSid(sid);
            student.setSname(sname);
            student.setSclass(stuClass.getClsid());
            student.setSmajor(major.getMid());

            // 填充到扩展类对象中
            studentExtend.setStudent(student);
            studentExtend.setStuClass(stuClass);
            studentExtend.setMajor(major);
            studentExtend.setStuCourse(stuCourse);

            studentExtendList.add(studentExtend);
        }

        // 插入或更新学生列表
        for (StudentExtend extend : studentExtendList) {
            // 获得同主键的学生、专业、班级对象
            Student testStudent = studentMapper.selectByPrimaryKey(extend.getStudent().getSid());
            Major testMajor = majorMapper.selectByPrimaryKey(extend.getMajor().getMid());
            StuClass testClass = stuClassMapper.selectByPrimaryKey(extend.getStuClass().getClsid());
            // 获得同课程号和学号的学生课程关系对象
            StuCourseExample stuCourseExample = new StuCourseExample();
            StuCourseExample.Criteria criteria = stuCourseExample.createCriteria();
            criteria.andCidEqualTo(extend.getStuCourse().getCid());
            criteria.andSidEqualTo(extend.getStuCourse().getSid());
            List<StuCourse> testList = stuCourseMapper.selectByExample(stuCourseExample);

            // 判断该major类对象是否存在，不存在则插入，存在则进一步比较是否不同需要修改
            if (testMajor == null) {
                majorMapper.insert(extend.getMajor());
            } else if (!testMajor.equals(extend.getMajor())) {
                majorMapper.updateByPrimaryKeySelective(extend.getMajor());
            }

            // 判断该班级类对象是否存在，不存在则插入，存在则进一步比较是否不同需要修改
            if (testClass == null) {
                stuClassMapper.insert(extend.getStuClass());
            } else if (!testClass.equals(extend.getStuClass())) {
                stuClassMapper.updateByPrimaryKeySelective(extend.getStuClass());
            }

            // 判断该学生类对象是否存在，不存在则插入，存在则进一步比较是否不同需要修改
            if (testStudent == null) {
                studentMapper.insert(extend.getStudent());
            } else if (!testStudent.equals(extend.getStudent())) {
                studentMapper.updateByPrimaryKeySelective(extend.getStudent());
            }

            // 判断该学生-课程对应关系是否存在，由于uuid是自动生成的，所以只需要比较内容是否相同
            // 如果不存在则插入；否则先判断课程属性！！
            // 选修：只要存在，不管与原来相同不相同都要在原来的score（此处用来存学分）基础上+现在的对象的score（学分）；之后删除原有的添加新的
            // 必修：如果和原来的相同则不用动；如果和原来的不同，则把原来的删掉再添加(否则由于uuid不同会导致重复)
            if (testList.size() == 0) {
                stuCourseMapper.insert(extend.getStuCourse());
            } else {
                // 非必修要获取原score值加上现在score的值来进行修改
                if (testList.get(0).getCid() == "88888888x" || testList.get(0).getCid() == "99999999x") {
                    double newCredit = testList.get(0).getScore() + extend.getStuCourse().getScore();
                    extend.getStuCourse().setScore(newCredit);

                    stuCourseMapper.deleteByPrimaryKey(testList.get(0).getScid());
                    stuCourseMapper.insert(extend.getStuCourse());
                } else if (!testList.get(0).equals(extend.getStuCourse())) {
                    // 必修，且与原来不相同，则删除原有的添加新的
                    stuCourseMapper.deleteByPrimaryKey(testList.get(0).getScid());
                    stuCourseMapper.insert(extend.getStuCourse());
                }
            }
        }
    }

    /**
     * @Description: 删除全部学生信息及相关班级专业信息(即清空学生表, 班级表和专业表)
     * @Param: []
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/22
     */
    public int deleteAllStusRelated() throws Exception {
        // 删除全部学生
        StudentExample studentExample = new StudentExample();
        studentExample.createCriteria().andSidIsNotNull();

        // 删除全部学生成绩
        StuCourseExample stuCourseExample = new StuCourseExample();
        stuCourseExample.createCriteria().andScidIsNotNull();

        // 删除全部班级
        StuClassExample stuClassExample = new StuClassExample();
        stuClassExample.createCriteria().andClsidIsNotNull();

        // 删除全部专业表
        MajorExample majorExample = new MajorExample();
        majorExample.createCriteria().andMidIsNotNull();

        try {
            List<Student> studentList = studentMapper.selectByExample(studentExample);
            List<StuCourse> stuCourseList = stuCourseMapper.selectByExample(stuCourseExample);
            List<StuClass> stuClassList = stuClassMapper.selectByExample(stuClassExample);
            List<Major> majorList = majorMapper.selectByExample(majorExample);

            // 数据已空
            if (studentList.size() == 0 || stuCourseList.size() == 0 ||
                    stuClassList.size() == 0 || majorList.size() == 0) {
                throw new SAException(ExceptionEnum.STUDENT_DATA_EMPTY);
            }

            int n1 = studentMapper.deleteByExample(studentExample);
            int n2 = stuClassMapper.deleteByExample(stuClassExample);
            int n3 = majorMapper.deleteByExample(majorExample);
            int n4 = stuCourseMapper.deleteByExample(stuCourseExample);

            if (n1 > 0 && n2 > 0 && n3 > 0 && n4 > 0) {
                return n1 + n2 + n3 + n4;
            }
            throw new SAException(ExceptionEnum.STUDENT_DATA_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 根据学号获取该学生的相关修课信息
     * @Param: [sid]
     * @return: List<StuCourseExtend>
     * @Author: StarryHu
     * @Date: 2019/3/23
     */
    public List<StuCourseExtend> getStuInfoBySid(String sid) throws Exception {
        // 获取学生对象，同时得到对应的专业号
        Student student = studentMapper.selectByPrimaryKey(sid);
        if (student == null) {
            throw new SAException(ExceptionEnum.STUDENT__NO_EXIST);
        }
        String smajor = student.getSmajor();
        // 根据专业号获取教学计划id
        String planId = majorMapper.selectByPrimaryKey(smajor).getMplan();
        // 根据教学计划id获取所需要修的全部课程 (使用扩展CourseExtendMapper查找)
        List<Course> mustCourses = courseExtendMapper.getMustCoursInPlan(planId);

        // 根据学生id获取其已修的全部必修课程、限选课程、公选课程
        List<StuCourseExtend> doneCourses0 = courseExtendMapper.getDoneCoursAcquBySid(sid);
        List<StuCourseExtend> doneCourses1 = courseExtendMapper.getDoneCoursNoAcquByKcsx(1);
        List<StuCourseExtend> doneCourses2 = courseExtendMapper.getDoneCoursNoAcquByKcsx(2);

        // 建立学生信息扩展对象
        StuInfoExtend stuInfoExtend = new StuInfoExtend();
        // 建立未修够课程数组
        List<StuCourseExtend> undoCourse = new ArrayList<>();

        // 使用双重for循环
        for (Course mustOne : mustCourses) {
            // 如果遍历到教学计划中的课程是必修课程，则进入学生已修的必修课程数组里面找
            if (mustOne.getKcsx() == 0) {
                boolean leaveAdvance = false;
                for (StuCourseExtend doneOne : doneCourses0) {
                    // 当两个课程号匹配的时候，动态判断里面的成绩信息，同时判断后可提前退出循环
                    if (mustOne.getCid().equals(doneOne.getCid())) {
                        if (doneOne.getScore() < 60) {
                            undoCourse.add(doneOne);
                            System.out.println("未修够" + doneOne.getCname() + "---" + doneOne.getCredit());
                        } else {
                            System.out.println("已修够" + doneOne.getCname() + "---" + doneOne.getCredit());
                        }
                        leaveAdvance = true;
                        break;
                    }
                }
                // 全部遍历完仍未找到，则将这门课程加入到未修够课程中
                if (!leaveAdvance) {
                    // 建立学生_课程关系对象并将数据填充进去
                    StuCourseExtend temp = new StuCourseExtend();
                    temp.setCid(mustOne.getCid());
                    temp.setCname(mustOne.getCname());
                    temp.setCredit(mustOne.getCredit());
                    temp.setScore(0);
                    temp.setKcsx(mustOne.getKcsx());
                    undoCourse.add(temp);
                }

            } else if (mustOne.getKcsx() == 1) {
                // 进入学生的限选课程，处理限选课程
                handleNoAcquiredCourses(doneCourses1, mustOne, undoCourse);
            } else {
                // 进入学生的公选课程，处理公选课程
                handleNoAcquiredCourses(doneCourses2, mustOne, undoCourse);
            }

        }
        return undoCourse;
    }

    /**
     * @Description: 处理非必修课程（两大类：1限选2公选）
     * @Param: [doneCourseNoAcquired, mustOne, undoCourse]
     * 参数分别为：此类非必修课程的学生课程关系数组，此时比较的教学计划课程对象，未修够学分的学生课程关系数组
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/23
     */
    private void handleNoAcquiredCourses(List<StuCourseExtend> doneCourseNoAcquired, Course mustOne,
                                         List<StuCourseExtend> undoCourse) {
        // 进入学生的某一类的非必修课程，进行学分累加（可以是限选/公选）
        // 只要累计修学分达到要求即可，不需要分数判断
        double sum = 0;
        for (StuCourseExtend doneOne : doneCourseNoAcquired) {
            sum += doneOne.getCredit();
        }
        if (sum >= mustOne.getKcsx()) {
            System.out.println("已修够" + mustOne.getCname() + "---" + mustOne.getCredit());
        } else {
            // 建立学生_课程关系对象并将数据填充进去
            StuCourseExtend temp = new StuCourseExtend();
            temp.setCid(mustOne.getCid());
            temp.setCname(mustOne.getCname());
            temp.setCredit(mustOne.getCredit() - sum);
            temp.setScore(0);
            temp.setKcsx(mustOne.getKcsx());

            // 加入未修课程数组中
            undoCourse.add(temp);
            System.out.println("未修够" + mustOne.getCname() + "---" + mustOne.getCredit());
        }
    }
}
