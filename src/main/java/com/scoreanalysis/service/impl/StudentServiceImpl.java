package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.*;
import com.scoreanalysis.dao.MajorMapper;
import com.scoreanalysis.dao.StuClassMapper;
import com.scoreanalysis.dao.StuCourseMapper;
import com.scoreanalysis.dao.StudentMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.enums.MajorEnum;
import com.scoreanalysis.exception.SAException;
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

    /** 
    * @Description: 上传学生成绩信息相关，将数据导入student表，stuClass表以及major表
    * @Param: [file] 
    * @return: void 
    * @Author: StarryHu
    * @Date: 2019/3/22 
    */ 
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void batchUpload(MultipartFile file) throws Exception {
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
//            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
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
//                    major.setMplan("20110200");

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

            // 课程属性；在课程属性是公选的时候设置特定的课程号;其他情况指定对应的课程属性代号
            String kcsxValue = row.getCell(9).getStringCellValue();
            if (kcsxValue == null || kcsxValue.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程属性未填写)");
            }
            if (kcsxValue.equals("公选")) {
                cid = "000000";
            }

            // 学生该门课程对应的成绩
            row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
            String scoreValue = row.getCell(10).getStringCellValue();
            if (scoreValue == null || scoreValue.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,课程成绩未填写)");
            }
            scoreValue = scoreValue.replace("-", "");
            int score = Integer.parseInt(scoreValue);

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
            // 如果不存在则插入；如果和原来的相同则不用动；如果和原来的不同，则把原来的删掉再添加
            if (testList.size() == 0) {
                stuCourseMapper.insert(extend.getStuCourse());
            } else if (!testList.get(0).equals(extend.getStuCourse())) {
                stuCourseMapper.deleteByPrimaryKey(testList.get(0).getScid());
                stuCourseMapper.insert(extend.getStuCourse());
            }
        }
    }

    /**
     * @Description: 删除全部学生信息及相关班级专业信息(即清空学生表,班级表和专业表)
     * @Param: []
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/22
     */
    public int deleteAllStusRelated() throws Exception {
        // 删除全部学生
        StudentExample studentExample = new StudentExample();
        studentExample.createCriteria().andSidIsNotNull();

        // 删除全部班级
        StuClassExample stuClassExample = new StuClassExample();
        stuClassExample.createCriteria().andClsidIsNotNull();

        // 删除全部专业表
        MajorExample majorExample = new MajorExample();
        majorExample.createCriteria().andMidIsNotNull();

        try {
            List<Student> studentList = studentMapper.selectByExample(studentExample);
            List<StuClass> stuClassList = stuClassMapper.selectByExample(stuClassExample);
            List<Major> majorList = majorMapper.selectByExample(majorExample);

            // 数据已空
            if (studentList.size() == 0 || stuClassList.size() == 0 || majorList.size() == 0) {
                throw new SAException(ExceptionEnum.STUDENT_DATA_EMPTY);
            }

            int n1 = studentMapper.deleteByExample(studentExample);
            int n2 = stuClassMapper.deleteByExample(stuClassExample);
            int n3 = majorMapper.deleteByExample(majorExample);

            if (n1 > 0 && n2 > 0 && n3 > 0) {
                return n1 + n2 + n3;
            }
            throw new SAException(ExceptionEnum.STUDENT_DATA_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }
}
