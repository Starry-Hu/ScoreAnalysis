package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.*;
import com.scoreanalysis.dao.*;
import com.scoreanalysis.daoExtend.CourseExtendMapper;
import com.scoreanalysis.daoExtend.StuInfoExtendMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.enums.MajorEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.pojo.StuCourseExtend;
import com.scoreanalysis.pojo.StuInfoExtend;
import com.scoreanalysis.pojo.StudentExtend;
import com.scoreanalysis.service.StudentService;
import com.scoreanalysis.util.IDGenerator;
import com.scoreanalysis.util.PageBean;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private StuInfoExtendMapper stuInfoExtendMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private JavaMailSender javaMailSender; //发送邮件相关

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
            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
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
            // 分情况赋予各类选修特定的课程号
            if (kcsxValue.equals("限选")) {
                cid = "88888888x";
            } else if (kcsxValue.equals("任选")) {
                cid = "99999999x";
            }

            // 学生该门课程对应的成绩(此处需要注意是否是必修/选修)
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


            // 填充到扩展类对象中
            studentExtend.setStudent(student);
            studentExtend.setStuClass(stuClass);
            studentExtend.setMajor(major);
            studentExtend.setStuCourse(stuCourse);

            studentExtendList.add(studentExtend);
        }

        // 设置第一次判断是否限选/公选,以便于后边遍历时在第一次满足条件时将原数据库的对应选修课程学分清0（此处对应score）
        boolean firstXuan1 = true, firstXuan2 = true;

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

            // 判断该专业类对象是否存在，不存在则插入，存在则进一步比较是否不同需要修改
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
            // 如果不存在则插入；否则先判断课程属性！！！
            // 选修：只要存在，不管与原来相同不相同都要在原来的score（此处用来存学分）基础上+现在的对象的score（学分）；之后删除原有的添加新的
            // 必修：如果和原来的相同则不用动；如果和原来的不同，则把原来的删掉再添加(否则由于uuid不同会导致重复)
            if (testList.size() == 0) {
                stuCourseMapper.insert(extend.getStuCourse());
            } else {
                // 非必修要获取原score值加上现在score的值来进行修改
                String testCidXuan = testList.get(0).getCid();
                if (testCidXuan.equals("88888888x") || testCidXuan.equals("99999999x")) {
                    // 判断是否是本次上传时第一次遍历到公选课程，如果是则将原数据库的对应公选学分清0，再累加
                    if (testCidXuan.equals("88888888x") && firstXuan1) {
                        firstXuan1 = false;
                        testList.get(0).setScore(0.0);
                    } else if (testCidXuan.equals("99999999x") && firstXuan2) {
                        firstXuan2 = false;
                        testList.get(0).setScore(0.0);
                    }

                    // 进行学分累加
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
    }

    // --------------------------------------------------- 通知相关 -----------------------------------------------------
    /**
     * 获取学生两种通知方式的内容
     * @param sid 学号
     * @return Map<String, String>
     * @throws Exception
     */
    public Map<String, String> getStuInformWay(String sid) throws Exception {
        Student student = studentMapper.selectByPrimaryKey(sid);
        String email = student.getEmail();
        String phone = student.getPhone();

        // 如果两个通知信息都不存在，则抛出相应异常
        if ((email==null && phone==null )|| email.equals("") && phone.equals("")){
            throw new SAException(ExceptionEnum.STU_INFORM_NULL);
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        // 如果是空则显示给前端为“”，否则照常显示即可
        resultMap.put("email", (email == null?"":email));
        resultMap.put("phone", (phone == null?"":phone));
        return resultMap;
    }

    /**
     * 发送邮件通知
     * @param fromEmail 目的方
     * @param toEmail 发送方
     * @param informContent 通知内容
     * @throws Exception
     */
    public void sendMailInform(String fromEmail,String toEmail,String informContent) throws Exception{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("毕业学分预警");
        message.setText(informContent);
        javaMailSender.send(message);
    }


    // --------------------------------------------------获取相关--------------------------------------------------------

    /**
     * @Description: 根据学号获取该学生的相关修课信息
     * @Param: [sid]
     * @return: List<StuCourseExtend>
     * @Author: StarryHu
     * @Date: 2019/3/23
     */
    public StuInfoExtend getStuInfoBySid(String sid) throws Exception {
        // 获取学生对象 -> 班级 -> 专业
        Student student = studentMapper.selectByPrimaryKey(sid);
        if (student == null) {
            throw new SAException(ExceptionEnum.STUDENT__NO_EXIST);
        }
        StuClass stuClass = stuClassMapper.selectByPrimaryKey(student.getSclass());
        String smajor = stuClass.getClsMajor();
        // 根据专业号获取教学计划id
        String planId = majorMapper.selectByPrimaryKey(smajor).getMplan();
        // 根据教学计划id获取所需要修的全部课程 (使用扩展CourseExtendMapper查找)
        List<Course> mustCourses = courseExtendMapper.getMustCoursInPlan(planId);

        // 根据学生id获取对应的教学计划中-已修的全部必修课程、限选课程、公选课程(0/1/2)
        List<StuCourseExtend> doneCourses0 = courseExtendMapper.getDoneCoursBySid(sid, 0, planId);
        List<StuCourseExtend> doneCourses1 = courseExtendMapper.getDoneCoursBySid(sid, 1, planId);
        List<StuCourseExtend> doneCourses2 = courseExtendMapper.getDoneCoursBySid(sid, 2, planId);

        // 建立未修够课程数组
        List<StuCourseExtend> undoCourse = new ArrayList<>();
        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        StuInfoExtend stuInfoExtend = stuInfoExtendMapper.getStuBasicInfoBySid(sid);
        stuInfoExtend.setUndoCourse(undoCourse);

        // 对学生已修课程与教学计划课程进行比较
        handleStuCourCompare(mustCourses, doneCourses0, doneCourses1, doneCourses2, undoCourse);

        return stuInfoExtend;
    }

    /**
     * @Description: 根据班级获取该班级全部学生的相关修课信息
     * @Param: [clsId]
     * @return: java.util.List<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    public List<StuInfoExtend> getStuInfoByStuCls(String clsId) throws Exception {
        // 根据班级号获取班级信息，同时获取专业号以此获取对应教学计划号
        StuClass stuClass = stuClassMapper.selectByPrimaryKey(clsId);
        if (stuClass == null) {
            throw new SAException(ExceptionEnum.CLASS_NO_EXIST);
        }
        String clsMajor = stuClass.getClsMajor();
        String planId = majorMapper.selectByPrimaryKey(clsMajor).getMplan();

        // 根据教学计划id获取所需要修的全部课程 (使用扩展CourseExtendMapper查找)
        List<Course> mustCourses = courseExtendMapper.getMustCoursInPlan(planId);

        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        List<StuInfoExtend> stuInfoExtendList = stuInfoExtendMapper.getAllStusBasicInfoByClsId(clsId);

        // 处理多学生（数组）的已修课程与教学计划要求全部课程的筛选
        handleMoreStusCourInSelect(stuInfoExtendList, planId, mustCourses);

        return stuInfoExtendList;
    }

    /**
     * @Description: 获取某个专业的全部学生的修课情况
     * @Param: [majorId]
     * @return: java.util.List<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    public List<StuInfoExtend> getStuInfoByMajor(String majorId) throws Exception {
        // 根据专业号获取对应专业 -> 教学计划id
        Major major = majorMapper.selectByPrimaryKey(majorId);
        if (major == null) {
            throw new SAException(ExceptionEnum.MAJOR_NO_EXIST);
        }
        String planId = major.getMplan();
        // 根据教学计划id获取所需要修的全部课程 (使用扩展CourseExtendMapper查找)
        List<Course> mustCourses = courseExtendMapper.getMustCoursInPlan(planId);

        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        List<StuInfoExtend> stuInfoExtendList = stuInfoExtendMapper.getAllStusBasicInfoByMajor(majorId);

        // 处理多学生（数组）的已修课程与教学计划要求全部课程的筛选
        handleMoreStusCourInSelect(stuInfoExtendList, planId, mustCourses);

        return stuInfoExtendList;
    }

    /**
     * @Description: 获取全部学生的修课情况
     * @Param: []
     * @return: java.util.List<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/28
     */
    public List<StuInfoExtend> getAllStuInfo() throws Exception {
        // 获取全部学生扩展信息数组
        List<StuInfoExtend> stuInfoExtendList = stuInfoExtendMapper.getAllStusBasicInfo();
        if (stuInfoExtendList.size() == 0) {
            throw new SAException(ExceptionEnum.STUDENT__NO_EXIST);
        }

        // 处理全部学生信息
        handleAllStuInfo(stuInfoExtendList);
        return stuInfoExtendList;
    }

    // ---------------------------------------------------分页相关-------------------------------------------------------

    /**
     * @Description: 获取某学生的修课情况（返回pagebean对象）分页默认为1
     * @Param: [sid, pageNum, pageSize]
     * @return: com.scoreanalysis.util.PageBean<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/28
     */
    public PageBean<StuInfoExtend> getStuInfoBySid(String sid, int pageNum, int pageSize) throws Exception {
        // 获取学生对象 -> 班级 -> 专业
        Student student = studentMapper.selectByPrimaryKey(sid);
        if (student == null) {
            throw new SAException(ExceptionEnum.STUDENT__NO_EXIST);
        }
        StuClass stuClass = stuClassMapper.selectByPrimaryKey(student.getSclass());
        String smajor = stuClass.getClsMajor();
        // 根据专业号获取教学计划id
        String planId = majorMapper.selectByPrimaryKey(smajor).getMplan();
        // 根据教学计划id获取所需要修的全部课程 (使用扩展CourseExtendMapper查找)
        List<Course> mustCourses = courseExtendMapper.getMustCoursInPlan(planId);

        // 根据学生id获取对应的教学计划中-已修的全部必修课程、限选课程、公选课程(0/1/2)
        List<StuCourseExtend> doneCourses0 = courseExtendMapper.getDoneCoursBySid(sid, 0, planId);
        List<StuCourseExtend> doneCourses1 = courseExtendMapper.getDoneCoursBySid(sid, 1, planId);
        List<StuCourseExtend> doneCourses2 = courseExtendMapper.getDoneCoursBySid(sid, 2, planId);

        // 建立未修够课程数组
        List<StuCourseExtend> undoCourse = new ArrayList<>();
        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        StuInfoExtend stuInfoExtend = stuInfoExtendMapper.getStuBasicInfoBySid(sid);
        stuInfoExtend.setUndoCourse(undoCourse);

        // 对学生已修课程与教学计划课程进行比较
        handleStuCourCompare(mustCourses, doneCourses0, doneCourses1, doneCourses2, undoCourse);

        // 建立pageBean对象
        PageBean<StuInfoExtend> pBean = new PageBean<>(1, 1, 1);
        List<StuInfoExtend> stuInfoExtendList = new ArrayList<>();
        stuInfoExtendList.add(stuInfoExtend);
        pBean.setList(stuInfoExtendList);

        return pBean;
    }

    /**
     * @Description: 根据班级获取该班级全部学生的相关修课信息(带分页)
     * @Param: [clsId, pageNum, pageSize] (班级id，当前页的大小，每页数)
     * @return: com.scoreanalysis.util.PageBean<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    public PageBean<StuInfoExtend> getStuInfoByStuClsWithPage(String clsId, int pageNum, int pageSize) throws Exception {
        // 根据班级号获取班级信息，同时获取专业号以此获取对应教学计划号
        StuClass stuClass = stuClassMapper.selectByPrimaryKey(clsId);
        if (stuClass == null) {
            throw new SAException(ExceptionEnum.CLASS_NO_EXIST);
        }
        String clsMajor = stuClass.getClsMajor();
        String planId = majorMapper.selectByPrimaryKey(clsMajor).getMplan();

        // 根据教学计划id获取所需要修的全部课程 (使用扩展CourseExtendMapper查找)
        List<Course> mustCourses = courseExtendMapper.getMustCoursInPlan(planId);

        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        // 拿到数据库里的全部数据 有多少拿多少（总数）
        List<StuInfoExtend> allDatas = stuInfoExtendMapper.getAllStusBasicInfoByClsId(clsId);


        // ---进行分页处理---
        // 获取全部数量  同时建立pagebean对象
        int totalRecord = allDatas.size();
        PageBean<StuInfoExtend> pBean = new PageBean<>(pageNum, pageSize, totalRecord);
        // 获取pagebean对象的startIndex
        int startIndex = pBean.getStartIndex();

        // 查找对应的数据
        List<StuInfoExtend> realDatas = stuInfoExtendMapper.getAllStusBasicInfoByClsIdWithPage(clsId, startIndex, pageSize);

        // 处理多学生（数组）的已修课程与教学计划要求全部课程的筛选[只处理指定页数个数的数据]
        handleMoreStusCourInSelect(realDatas, planId, mustCourses);

        // 设置list对应的数据对象
        pBean.setList(realDatas);
        return pBean;
    }


    /**
     * @Description: 根据专业获取该专业全部学生的相关修课信息(带分页)
     * @Param: [majorId, pageNum, pageSize] (专业id，当前页的大小，每页数)
     * @return: com.scoreanalysis.util.PageBean<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    public PageBean<StuInfoExtend> getStuInfoByMajorWithPage(String majorId, int pageNum, int pageSize) throws Exception {
        // 根据专业号获取对应专业 -> 教学计划id
        Major major = majorMapper.selectByPrimaryKey(majorId);
        if (major == null) {
            throw new SAException(ExceptionEnum.MAJOR_NO_EXIST);
        }
        String planId = major.getMplan();
        // 根据教学计划id获取所需要修的全部课程 (使用扩展CourseExtendMapper查找)
        List<Course> mustCourses = courseExtendMapper.getMustCoursInPlan(planId);

        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        List<StuInfoExtend> allDatas = stuInfoExtendMapper.getAllStusBasicInfoByMajor(majorId);

        // ---进行分页处理---
        // 获取全部数量  同时建立pagebean对象
        int totalRecord = allDatas.size();
        PageBean<StuInfoExtend> pBean = new PageBean<>(pageNum, pageSize, totalRecord);
        // 获取pagebean对象的startIndex
        int startIndex = pBean.getStartIndex();

        // 查找对应的数据
        List<StuInfoExtend> realDatas = stuInfoExtendMapper.getAllStusBasicInfoByMajorWithPage(majorId, startIndex, pageSize);

        // 处理多学生（数组）的已修课程与教学计划要求全部课程的筛选[只处理指定页数个数的数据]
        handleMoreStusCourInSelect(realDatas, planId, mustCourses);

        // 设置list对应的数据对象
        pBean.setList(realDatas);
        return pBean;
    }

    /**
     * @Description: 获取全部学生的修课情况[带分页]
     * @Param: []
     * @return: java.util.List<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/28
     */
    public PageBean<StuInfoExtend> getAllStuInfoWithPage(int pageNum, int pageSize) throws Exception {
        // 获取全部学生扩展信息数组
        List<StuInfoExtend> allDatas = stuInfoExtendMapper.getAllStusBasicInfo();

        // ---进行分页处理---
        // 获取全部数量  同时建立pagebean对象
        int totalRecord = allDatas.size();
        if (allDatas.size() == 0) {
            throw new SAException(ExceptionEnum.STUDENT__NO_EXIST);
        }
        PageBean<StuInfoExtend> pBean = new PageBean<>(pageNum, pageSize, totalRecord);
        // 获取pagebean对象的startIndex
        int startIndex = pBean.getStartIndex();

        // 查找对应的数据
        List<StuInfoExtend> realDatas = stuInfoExtendMapper.getAllStusBasicInfoWithPage(startIndex, pageSize);

        // 处理分页后的全部学生信息
        handleAllStuInfo(realDatas);
        // 设置list对应的数据对象
        pBean.setList(realDatas);
        return pBean;
    }


    // --------------------------------------------------英语相关--------------------------------------------------------

    /**
     * @Description: 根据学号查找某个学生的大英4情况(返回pageBean对象 ， 方便统一分页)赋值1
     * @Param: [sid, pageNum, pageSize]
     * @return: com.scoreanalysis.util.PageBean<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/29
     */
    public PageBean<StuInfoExtend> getStuInfoBySidInEng4(String sid) throws Exception {
        // 获取学生对象 -> 班级 -> 专业
        Student student = studentMapper.selectByPrimaryKey(sid);
        if (student == null) {
            throw new SAException(ExceptionEnum.STUDENT__NO_EXIST);
        }
        StuClass stuClass = stuClassMapper.selectByPrimaryKey(student.getSclass());
        String smajor = stuClass.getClsMajor();
        // 根据专业号获取教学计划id
        String planId = majorMapper.selectByPrimaryKey(smajor).getMplan();

        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        // 拿到数据库里的全部数据 有多少拿多少（总数）
        StuInfoExtend stuInfoExtend = stuInfoExtendMapper.getStuBasicInfoBySid(sid);


        // ---进行分页处理---
        // 获取全部数量  同时建立pagebean对象
        PageBean<StuInfoExtend> pBean = new PageBean<>(1, 1, 1);
        // 获取pagebean对象的startIndex
        int startIndex = pBean.getStartIndex();

        // 将一个学生对象放入数组中，方便分页使用
        List<StuInfoExtend> stuInfoExtendList = new ArrayList<>();
        stuInfoExtendList.add(stuInfoExtend);

        // 处理学生数组的大英4情况（分页的部分数据）
        handleStuInfoInEng4(stuInfoExtendList, "80710004");
        // 设置list对应的数据对象
        pBean.setList(stuInfoExtendList);

        return pBean;
    }

    /**
     * @Description: 根据班级获取全部学生的大英4情况(带分页)
     * @Param: [clsId, pageNum, pageSize]
     * @return: com.scoreanalysis.util.PageBean<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/29
     */
    public PageBean<StuInfoExtend> getStuInfoByClsIdWithPageInEng4(String clsId, int pageNum, int pageSize) throws Exception {
        // 根据班级号获取班级信息，同时获取专业号以此获取对应教学计划号
        StuClass stuClass = stuClassMapper.selectByPrimaryKey(clsId);
        if (stuClass == null) {
            throw new SAException(ExceptionEnum.CLASS_NO_EXIST);
        }
        String clsMajor = stuClass.getClsMajor();
        String planId = majorMapper.selectByPrimaryKey(clsMajor).getMplan();


        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        // 拿到数据库里的全部数据 有多少拿多少（总数）
        List<StuInfoExtend> allDatas = stuInfoExtendMapper.getAllStusBasicInfoByClsId(clsId);


        // ---进行分页处理---
        // 获取全部数量  同时建立pagebean对象
        int totalRecord = allDatas.size();
        PageBean<StuInfoExtend> pBean = new PageBean<>(pageNum, pageSize, totalRecord);
        // 获取pagebean对象的startIndex
        int startIndex = pBean.getStartIndex();

        // 查找对应的数据
        List<StuInfoExtend> realDatas = stuInfoExtendMapper.getAllStusBasicInfoByClsIdWithPage(clsId, startIndex, pageSize);

        // 处理学生数组的大英4情况（分页的部分数据）
        handleStuInfoInEng4(realDatas, "80710004");
        // 设置list对应的数据对象
        pBean.setList(realDatas);

        return pBean;
    }

    /**
     * @Description: 根据专业获取全部学生的大英4信息（带分页）
     * @Param: [majorId, pageNum, pageSize]
     * @return: com.scoreanalysis.util.PageBean<com.scoreanalysis.pojo.StuInfoExtend>
     * @Author: StarryHu
     * @Date: 2019/3/29
     */
    public PageBean<StuInfoExtend> getStuInfoByMajorWithPageInEng4(String majorId, int pageNum, int pageSize) throws Exception {
        // 根据专业号获取对应专业 -> 教学计划id
        Major major = majorMapper.selectByPrimaryKey(majorId);
        if (major == null) {
            throw new SAException(ExceptionEnum.MAJOR_NO_EXIST);
        }
        String planId = major.getMplan();

        // 通过学生id直接获取到学生信息扩展对象（已填充基本信息）
        // 拿到数据库里的全部数据 有多少拿多少（总数）
        List<StuInfoExtend> allDatas = stuInfoExtendMapper.getAllStusBasicInfoByMajor(majorId);


        // ---进行分页处理---
        // 获取全部数量  同时建立pagebean对象
        int totalRecord = allDatas.size();
        PageBean<StuInfoExtend> pBean = new PageBean<>(pageNum, pageSize, totalRecord);
        // 获取pagebean对象的startIndex
        int startIndex = pBean.getStartIndex();

        // 查找对应的数据
        List<StuInfoExtend> realDatas = stuInfoExtendMapper.getAllStusBasicInfoByMajorWithPage(majorId, startIndex, pageSize);

        // 处理学生数组的大英4情况（分页的部分数据）
        handleStuInfoInEng4(realDatas, "80710004");
        // 设置list对应的数据对象
        pBean.setList(realDatas);

        return pBean;
    }

    /**
     * @Description: 获取全部学生信息的大英4情况（带分页）
     * @Param: [pageNum, pageSize]
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/29
     */
    public PageBean<StuInfoExtend> getAllStuInfoWithPageInEng4(int pageNum, int pageSize) throws Exception {
        // 获取全部学生扩展信息数组
        List<StuInfoExtend> allDatas = stuInfoExtendMapper.getAllStusBasicInfo();

        // ---进行分页处理---
        // 获取全部数量  同时建立pagebean对象
        int totalRecord = allDatas.size();
        if (allDatas.size() == 0) {
            throw new SAException(ExceptionEnum.STUDENT__NO_EXIST);
        }
        PageBean<StuInfoExtend> pBean = new PageBean<>(pageNum, pageSize, totalRecord);
        // 获取pagebean对象的startIndex
        int startIndex = pBean.getStartIndex();

        // 查找对应的数据
        List<StuInfoExtend> realDatas = stuInfoExtendMapper.getAllStusBasicInfoWithPage(startIndex, pageSize);

        // 处理学生数组的大英4情况（分页的部分数据）
        handleStuInfoInEng4(realDatas, "80710004");
        // 设置list对应的数据对象
        pBean.setList(realDatas);

        return pBean;
    }


    // --------------------------------------------------内部使用--------------------------------------------------------

    /**
     * @Description: 处理多学生（数组）[主要针对根据班级、专业查找情况(同一教学计划)] 的已修课程与教学计划要求全部课程的筛选
     * @Param: [stuInfoExtendList, planId, mustCourses]
     * 参数分别为：学生信息扩展对象数组，所属教学计划id，教学计划要求全部课程数组
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    private void handleMoreStusCourInSelect(List<StuInfoExtend> stuInfoExtendList, String planId, List<Course> mustCourses) {
        for (StuInfoExtend extend : stuInfoExtendList) {
            // 建立未修够课程数组
            List<StuCourseExtend> undoCourse = new ArrayList<>();
            extend.setUndoCourse(undoCourse);
            String sid = extend.getSid();

            // 根据学生id获取对应的教学计划中-已修的全部必修课程、限选课程、公选课程(0/1/2)
            List<StuCourseExtend> doneCourses0 = courseExtendMapper.getDoneCoursBySid(sid, 0, planId);
            List<StuCourseExtend> doneCourses1 = courseExtendMapper.getDoneCoursBySid(sid, 1, planId);
            List<StuCourseExtend> doneCourses2 = courseExtendMapper.getDoneCoursBySid(sid, 2, planId);

            // 对学生已修课程与教学计划课程进行比较
            handleStuCourCompare(mustCourses, doneCourses0, doneCourses1, doneCourses2, undoCourse);
        }
    }


    /**
     * @Description: 对学生已修课程与教学计划课程进行比较
     * @Param: [mustCourses, doneCourses0, doneCourses1, doneCourses2, undoCourse]
     * 参数分别为：教学计划要求的全部课程数组，学生已修的全部必修课程数组，学生已修的全部限选课程数组，学生已修的全部公选课程数组，存储-学生未修够要求课程数组
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/24
     */
    private void handleStuCourCompare(List<Course> mustCourses, List<StuCourseExtend> doneCourses0,
                                      List<StuCourseExtend> doneCourses1, List<StuCourseExtend> doneCourses2, List<StuCourseExtend> undoCourse) {
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
    }

    /**
     * @Description: 处理非必修课程（两大类：1限选2公选）
     * @Param: [doneCourseNoAcquired, mustOne, undoCourse]
     * 参数分别为：学生的此类非必修课程的学生课程关系数组(一个元素，如不空对其操作即可)，教学计划标准对象，未修够学分的学生课程关系数组
     * 需要注意！！！非必修课程对应的学生课程关系对象里，score为其修得的学分而非credit！（credit为教学计划要求的此类选修课需要的学分）
     * 因此如果这个对象存在则用这个对象的score与credit两两比较即可
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/23
     */
    private void handleNoAcquiredCourses(List<StuCourseExtend> doneCourseNoAcquired, Course mustOne,
                                         List<StuCourseExtend> undoCourse) {
        // 如果学生对应的该类选修课程数组长度为0（未选过此类选修），则直接添加到undoCourse
        if (doneCourseNoAcquired.size() == 0) {
            // 建立学生_课程关系对象并将数据填充进去
            StuCourseExtend temp = new StuCourseExtend();
            temp.setCid(mustOne.getCid());
            temp.setCname(mustOne.getCname());
            temp.setCredit(mustOne.getCredit()); // 要求的学分
            temp.setScore(mustOne.getCredit());// 现在已修的学分
            temp.setKcsx(mustOne.getKcsx());

            // 加入未修课程数组中
            undoCourse.add(temp);
            System.out.println("未修够" + mustOne.getCname() + "---" + mustOne.getCredit());
            return;
        }

        // 进入学生的某一类的非必修课程，进行学分累加（可以是限选/公选）
        // 只要累计修学分达到要求即可，不需要分数判断
        StuCourseExtend doneOne = doneCourseNoAcquired.get(0);
        // 已修学分与教学计划要求学分比较
        if (doneOne.getScore() >= doneOne.getCredit()) {
            System.out.println("已修够" + doneOne.getCname() + "---" + doneOne.getScore());
        } else {
            // 建立学生_课程关系对象并将数据填充进去
            StuCourseExtend temp = new StuCourseExtend();
            temp.setCid(doneOne.getCid());
            temp.setCname(doneOne.getCname());
            temp.setCredit(doneOne.getCredit()); // 要求的学分
            temp.setScore(doneOne.getScore()); // 现在已修的学分
            temp.setKcsx(doneOne.getKcsx());

            // 加入未修课程数组中
            undoCourse.add(temp);
            System.out.println("未修够" + doneOne.getCname() + "---" + doneOne.getCredit());
        }
    }


    /**
     * @Description: 处理全部学生信息扩展对象（注意每次动态获取planId，不同于上述子方法的公用一个planId）
     * @Param: [stuInfoExtendList] 所需要处理的全部学生信息扩展对象数组
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/28
     */
    private void handleAllStuInfo(List<StuInfoExtend> stuInfoExtendList) {
        for (StuInfoExtend stuInfoExtend : stuInfoExtendList) {
            // 获取学号
            String sid = stuInfoExtend.getSid();
            // 获取学生的班级信息 -> 专业信息 -> 教学计划
            String sclass = studentMapper.selectByPrimaryKey(sid).getSclass();
            String smajor = stuClassMapper.selectByPrimaryKey(sclass).getClsMajor();
            String planId = majorMapper.selectByPrimaryKey(smajor).getMplan();
            // 根据教学计划id获取所需要修的全部课程 (使用扩展CourseExtendMapper查找)
            List<Course> mustCourses = courseExtendMapper.getMustCoursInPlan(planId);

            // 根据学生id获取对应的教学计划中-已修的全部必修课程、限选课程、公选课程(0/1/2)
            List<StuCourseExtend> doneCourses0 = courseExtendMapper.getDoneCoursBySid(sid, 0, planId);
            List<StuCourseExtend> doneCourses1 = courseExtendMapper.getDoneCoursBySid(sid, 1, planId);
            List<StuCourseExtend> doneCourses2 = courseExtendMapper.getDoneCoursBySid(sid, 2, planId);
            // 建立未修够课程数组
            List<StuCourseExtend> undoCourse = new ArrayList<>();
            stuInfoExtend.setUndoCourse(undoCourse);

            // 对学生已修课程与教学计划课程进行比较
            handleStuCourCompare(mustCourses, doneCourses0, doneCourses1, doneCourses2, undoCourse);
        }
    }

    /**
     * @Description: 处理学生数组的大英4成绩情况
     * @Param: [stuInfoExtendList, eng4Id]
     * @return: void
     * @Author: StarryHu
     * @Date: 2019/3/29
     */
    private void handleStuInfoInEng4(List<StuInfoExtend> stuInfoExtendList, String eng4Id) {
        for (StuInfoExtend stuInfoExtend : stuInfoExtendList) {
            // 获取学号
            String sid = stuInfoExtend.getSid();
            // 获取学生的班级信息 -> 专业信息 -> 教学计划
            String sclass = studentMapper.selectByPrimaryKey(sid).getSclass();
            String smajor = stuClassMapper.selectByPrimaryKey(sclass).getClsMajor();
            String planId = majorMapper.selectByPrimaryKey(smajor).getMplan();

            // 在教学计划中查找大英4是否有要求,如果未要求则抛出课程
            CourseExample courseExample = new CourseExample();
            courseExample.createCriteria().andCidEqualTo(eng4Id).andPlanIdEqualTo(planId);
            List<Course> courseList = courseMapper.selectByExample(courseExample);
            if (courseList.size() == 0) {
                throw new SAException(ExceptionEnum.COURSE_NO_EXIST);
            }
            Course eng4Course = courseList.get(0);

            // 查找对应学生大英4的成绩,同时建立学生课程扩展对象以便之后存储
            StuCourseExample stuCourseExample = new StuCourseExample();
            stuCourseExample.createCriteria().andSidEqualTo(sid).andCidEqualTo(eng4Id);
            List<StuCourse> stuCourseList = stuCourseMapper.selectByExample(stuCourseExample);
            StuCourseExtend stuCourseExtend = new StuCourseExtend();

            // 建立未修够课程数组
            List<StuCourseExtend> undoCourse = new ArrayList<>();
            // 未修过的情况
            if (stuCourseList.size() == 0) {
                stuCourseExtend.setCid(eng4Course.getCid());
                stuCourseExtend.setCname(eng4Course.getCname());
                stuCourseExtend.setCredit(eng4Course.getCredit());
                stuCourseExtend.setKcsx(eng4Course.getKcsx());
                stuCourseExtend.setScore(0.0);
                // 添加到未修课程中
                undoCourse.add(stuCourseExtend);
            } else {
                // 获取对应的学生-课程关系对象
                StuCourse stuCourse = stuCourseList.get(0);
                if (stuCourse.getScore() < 60) {
                    stuCourseExtend.setCid(eng4Course.getCid());
                    stuCourseExtend.setCname(eng4Course.getCname());
                    stuCourseExtend.setCredit(eng4Course.getCredit());
                    stuCourseExtend.setKcsx(eng4Course.getKcsx());
                    stuCourseExtend.setScore(stuCourse.getScore());
                    // 添加到未修课程中
                    undoCourse.add(stuCourseExtend);

                }
            }
            // 学生信息扩展对象设置未修课程字段
            stuInfoExtend.setUndoCourse(undoCourse);
        }
    }
}
