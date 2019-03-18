package com.scoreanalysis.controller;

import com.scoreanalysis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Project scoreanalysis
 * @ClassName StudentController
 * @Author StarryHu
 * @Description 学生相关接口
 * @Date 2019/3/10 18:12
 */
@RequestMapping("student")
public class StudentController extends BaseController{
    @Autowired
    private StudentService studentService;


}
