package com.scoreanalysis.controller;

import com.scoreanalysis.bean.Course;
import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.CourseService;
import com.scoreanalysis.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project scoreanalysis
 * @ClassName CourseController
 * @Author StarryHu
 * @Description 课程相关接口
 * @Date 2019/3/5 17:39
 */

@RestController
@RequestMapping("course")
public class CourseController extends BaseController {
    @Autowired
    private CourseService courseService;

    /**
    * @Description: 添加课程
    * @Param: [cid, cname, credit] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    @PostMapping("/add")
    public BaseResponse addCourse(String cid, String cname,Double credit) throws Exception{
        // 检查信息是否填写完全
        if (cid == null || cname == null || credit == null
                || cid.trim().equals("") || cname.trim().equals("") || credit == 0){
            return ajaxFail(ResultEnum.COURSE_INFO_NOT_FULL);
        }
        // 用内部查找方法查找相同id的课程
        Course course = courseService.findCourseById(cid);
        // 返回课程已存在异常
        if (course != null){
            return ajaxFail(ResultEnum.COURSE_EXIST);
        }
        
        // 执行添加
        courseService.addCourse(cid,cname,credit);
        return ajaxSucc(null,ResultEnum.COURSE_ADD_SUCCESS);
    }

    /**
    * @Description: 删除课程
    * @Param: [cid] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    @GetMapping("/delete")
    public BaseResponse deleteCourse(String cid) throws Exception{
        // 检查信息是否填写完全
        if (cid == null || cid.trim().equals("")){
            return ajaxFail(ResultEnum.COURSE_INFO_NOT_FULL);
        }
        // 查找原课程是否存在
        Course course = courseService.getCourseById(cid);
        // 删除课程
        courseService.deleteCourseLogic(cid);
        return ajaxSucc(null,ResultEnum.COURSE_DELETE_SUCCESS);
    }

    /**
    * @Description:
    * @Param: [cid, cname, credit]
    * @return: com.scoreanalysis.util.BaseResponse
    * @Author: StarryHu
    * @Date: 2019/3/5
    */
    @PostMapping("/update")
    public BaseResponse updateCourse(String cid,String cname,Double credit) throws Exception{
        // 检查信息是否填写完全
        if (cid == null || cname == null || credit == null
                || cid.trim().equals("") || cname.trim().equals("") || credit == 0){
            return ajaxFail(ResultEnum.COURSE_INFO_NOT_FULL);
        }
        // 查找该id课程
        Course course = courseService.getCourseById(cid);
        // 执行更新
        courseService.updateCourse(cid,cname,credit);
        return ajaxSucc(null,ResultEnum.COURSE_UPDATE_SUCCESS);
    }

    /** 
    * @Description: 根据id查找课程信息
    * @Param: [cid] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    @GetMapping("/search")
    public BaseResponse getCourseById(String cid) throws Exception{
        // 检查信息是否填写完全
        if (cid == null || cid.trim().equals("")){
            return ajaxFail(ResultEnum.COURSE_INFO_NOT_FULL);
        }
        // 查找对应id课程信息
        Course course = courseService.getCourseById(cid);
        return ajaxSucc(course,ResultEnum.COURSE_SEARCH_SUCCESS);
    }
}
