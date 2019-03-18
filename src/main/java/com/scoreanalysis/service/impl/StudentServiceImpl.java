package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.StuCourse;
import com.scoreanalysis.bean.StuCourseExample;
import com.scoreanalysis.bean.Student;
import com.scoreanalysis.bean.StudentExample;
import com.scoreanalysis.dao.StuCourseMapper;
import com.scoreanalysis.dao.StudentMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.StudentService;
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
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.STU_COURSE_ADD_FAIL);
        }catch (Exception e){
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
            if (n > 0 ){
                return n;
            } throw new SAException(ExceptionEnum.STU_COURSE_DELETE_FAIL);
        }catch (Exception e){
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
            int n = stuCourseMapper.updateByExampleSelective(stuCourse,example);
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.STU_COURSE_ADD_FAIL);
        }catch (Exception e){
            throw e;
        }
    }




    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean batchUpload(String fileName, MultipartFile file, boolean isExcel2003) throws Exception {
        boolean notNull = false;
        List<Student> studentList = new ArrayList<>();

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

        Student student;
        for (int r = 2; r <= sheet.getLastRowNum(); r++) {
            //sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException
            //r = 2 表示从第三行开始循环 如果你的第三行开始是数据
            Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
            if (row == null) {
                continue;
            }

            // 判断每个单元格的值，同时生成对象
            student = new Student();

            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String sid = row.getCell(0).getStringCellValue();
            if(sid==null || sid.isEmpty()){
                throw new Exception("导入失败(第"+(r+1)+"行,学号未填写)");
            }

            if( row.getCell(1).getCellType() !=1){
                throw new Exception("导入失败(第"+(r+1)+"行,姓名请设为文本格式)");
            }
            String sname = row.getCell(0).getStringCellValue();

            if(sname == null || sname.isEmpty()){
                throw new Exception("导入失败(第"+(r+1)+"行,姓名未填写)");
            }

            String add = row.getCell(2).getStringCellValue();
            if(add==null){
                throw new Exception("导入失败(第"+(r+1)+"行,不存在此单位或单位未填写)");
            }
            //完整的循环一次 就组成了一个对象
            student.setSid(sid);
            student.setSname(sname);
            studentList.add(student);
        }
        // 插入或更新学生列表
        for (Student studentRecord : studentList) {
            String sid = studentRecord.getSid();
            Student test = studentMapper.selectByPrimaryKey(sid);
            if (test == null) {
                studentMapper.insert(studentRecord);
                System.out.println(" 插入 " + studentRecord);
            } else {
                studentMapper.updateByPrimaryKeySelective(studentRecord);
                System.out.println(" 更新 " + studentRecord);
            }
        }

        return notNull;
    }

}
