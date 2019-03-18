package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.Course;
import com.scoreanalysis.dao.CourseMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project scoreanalysis
 * @ClassName AdminServiceImpl
 * @Author StarryHu
 * @Description 课程相关业务逻辑层
 * @Date 2019/2/9 19:20
 */

@Service(value = "courseService")
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 添加课程信息
     * @param cid 课程号
     * @param cname 课程名称
     * @param credit 课程学分
     * @return
     */
    public int addCourse(String cid,String cname,double credit) throws Exception{
        Course course = new Course();
        course.setCid(cid);
        course.setCname(cname);
        course.setCredit(credit);

        try {
            int n = courseMapper.insert(course);
            // 添加成功
            if (n > 0){
                return n;
            }
            throw new SAException(ExceptionEnum.COURSE_ADD_FAIL);
        }catch (Exception e){
            throw e;
        }
    }
    /**
    * @Description: 根据课程号物理删除课程
    * @Param: [cid] 课程号
    * @return: int
    * @Author: StarryHu
    * @Date: 2019/3/5
    */
    public int deleteCourse(String cid) throws Exception{
        try {
            int n = courseMapper.deleteByPrimaryKey(cid);
            // 删除成功
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.COURSE_DELETE_FAIL);
        }catch (Exception e){
            throw e;
        }
    }

    /**
    * @Description: 根据课程id更新课程信息
    * @Param: [cid, cname, credit]
    * @return: int
    * @Author: StarryHu
    * @Date: 2019/3/5
    */
    public int updateCourse(String cid,String cname,double credit){
        Course course = new Course();
        course.setCid(cid);
        course.setCname(cname);
        course.setCredit(credit);

        try {
            int n = courseMapper.updateByPrimaryKeySelective(course);
            // 更新成功
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.COURSE_UPDATE_FAIL);
        }catch (Exception e){
            throw e;
        }
    }
    /**
    * @Description: 根据课程id查找课程信息
    * @Param: [cid] 
    * @return: com.scoreanalysis.bean.Course 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    public Course getCourseById(String cid) throws Exception{
        Course course = courseMapper.selectByPrimaryKey(cid);
        // 课程不存在时抛异常
        if (course == null) {
            throw new SAException(ExceptionEnum.COURSE_NO_EXIST);
        }
        return courseMapper.selectByPrimaryKey(cid);
    }


    /**
    * @Description: 为内部使用的查找课程方法  无异常的抛出
    * @Param: [cid] 课程id
    * @return: com.scoreanalysis.bean.Course
    * @Author: StarryHu
    * @Date: 2019/3/5
    */
    public Course findCourseById(String cid){
        Course course = courseMapper.selectByPrimaryKey(cid);
        if (course == null){
            return null;
        }
        return course;
    }
}
