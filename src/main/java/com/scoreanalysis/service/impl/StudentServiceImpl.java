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
     * @Description: 添加学生信息
     * @Param: [sid, sname, sclass, smajor]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    @Override
    public int addStudent(String sid, String sname, String sclass, String smajor) throws Exception {
        Student student = new Student();
        student.setSid(sid);
        student.setSname(sname);
        student.setSclass(sclass);
        student.setSmajor(smajor);

        try {
            int n = studentMapper.insert(student);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.STUDENT_ADD_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * @Description: 根据学号删除学生信息
     * @Param: [sid]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    @Override
    public int deleteStudentBySid(String sid) throws Exception {
        Student student = new Student();
        student.setSid(sid);

        try {
            int n = studentMapper.deleteByPrimaryKey(sid);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.STUDENT_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 删除全部学生信息
     * @Param: []
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    @Override
    public int deleteAllStus() throws Exception {
        StudentExample example = new StudentExample();
        StudentExample.Criteria criteria = example.createCriteria();
        // 删除学号不为空的学生信息（全部学生信息）
        criteria.andSidIsNotNull();
        try {
            int n = studentMapper.deleteByExample(example);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.STUDENT_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 更新学生信息
     * @Param: [sid, sname, sclass, smajor]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    @Override
    public int updateStudent(String sid, String sname, String sclass, String smajor) throws Exception {
        Student student = new Student();
        student.setSid(sid);
        student.setSname(sname);
        student.setSclass(sclass);
        student.setSmajor(smajor);

        try {
            int n = studentMapper.updateByPrimaryKey(student);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.STUDENT_UPDATE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * @Description: 添加学生课程关系
     * @Param: [scid, sid, cid, score]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    @Override
    public int addStuCourse(String scid, String sid, String cid, int score) throws Exception {
        StuCourse stuCourse = new StuCourse();
        stuCourse.setScid(scid);
        stuCourse.setSid(sid);
        stuCourse.setCid(cid);
        stuCourse.setScore(score);

        try {
            int n = stuCourseMapper.insert(stuCourse);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.STU_COURSE_ADD_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 根据学生学号删除其对应的课程关系
     * @Param: [sid]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    @Override
    public int deleteStuCourse(String sid) throws Exception {
        StuCourseExample example = new StuCourseExample();
        StuCourseExample.Criteria criteria = example.createCriteria();
        criteria.andSidEqualTo(sid);
        try {
            int n = stuCourseMapper.deleteByExample(example);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.STU_COURSE_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 根据学生id和课程id来更新对应关系
     * @Param: [scid, sid, cid, score]
     * @return: int
     * @Author: StarryHu
     * @Date: 2019/3/10
     */
    @Override
    public int updateStuCourse(String scid, String sid, String cid, int score) throws Exception {
        // 根据学生id和课程id来更新
        StuCourseExample example = new StuCourseExample();
        StuCourseExample.Criteria criteria = example.createCriteria();
        criteria.andSidEqualTo(sid);
        criteria.andCidEqualTo(cid);

        StuCourse stuCourse = new StuCourse();
        stuCourse.setScore(score);

        try {
            int n = stuCourseMapper.updateByExampleSelective(stuCourse, example);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.STU_COURSE_ADD_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean batchUpload(String fileName, MultipartFile file, boolean isExcel2003) throws Exception {
        boolean notNull = false;
        List<StudentExtend> studentExtendList = new ArrayList<>();

        InputStream is = file.getInputStream();
        Workbook wb = WorkbookFactory.create(is);
//        Workbook wb = null;
//        if (isExcel2003) {
//            wb = new HSSFWorkbook(is);
//        } else {
//            wb = new XSSFWorkbook(is);
//        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            notNull = true;
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
            if (row.getCell(1).getCellType() != 1) {
                throw new Exception("导入失败(第" + (r + 1) + "行,姓名请设为文本格式)");
            }
            if (sname == null || sname.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,姓名未填写)");
            }

            // 班级
            String stuClassValue = row.getCell(3).getStringCellValue();
            if (stuClassValue == null || stuClassValue.isEmpty()) {
                throw new Exception("导入失败(第" + (r + 1) + "行,班级未填写)");
            }

            // 建立班级对象和专业对象
            StuClass stuClass = new StuClass();
            Major major = new Major();
            for (MajorEnum majorEnum : MajorEnum.values()){
                if (stuClassValue.contains(majorEnum.getMname())){
                    // 取专业枚举值填充专业对象
                    major.setMid(majorEnum.getMid());
                    major.setMname(majorEnum.getMname());
                    major.setMplan("123");

                    // 取值填充班级类
                    // 将专业名替代后剩下的部分为班级id,同时并上专业id(e.g:011701/计科1701)
                    stuClass.setClsid(major.getMid() + stuClassValue.replace(majorEnum.getMname(),""));
                    stuClass.setClsMajor(majorEnum.getMid());
                    stuClass.setClsName(stuClassValue);
                    break;
                }
            }

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

            studentExtendList.add(studentExtend);
        }

        // 插入或更新学生列表
        for (StudentExtend extend : studentExtendList) {
            Student testStudent = studentMapper.selectByPrimaryKey(extend.getStudent().getSid());
            Major testMajor = majorMapper.selectByPrimaryKey(extend.getMajor().getMid());
            StuClass testClass = stuClassMapper.selectByPrimaryKey(extend.getStuClass().getClsid());

            // 判断该major类对象是否存在
            if (testMajor == null){
                majorMapper.insert(extend.getMajor());
            }else {
                majorMapper.updateByPrimaryKeySelective(extend.getMajor());
            }

            // 判断该班级类对象是否存在
            if (testClass == null){
                stuClassMapper.insert(extend.getStuClass());
            }else{
                stuClassMapper.updateByPrimaryKeySelective(extend.getStuClass());
            }

            // 判断该学生类对象是否存在
            if (testStudent == null){
                studentMapper.insert(extend.getStudent());
                System.out.println(" 插入 " + extend.getStudent());
            }else {
                studentMapper.updateByPrimaryKeySelective(extend.getStudent());
                System.out.println(" 更新 " + extend.getStudent());
            }
        }

        return notNull;
    }

}
