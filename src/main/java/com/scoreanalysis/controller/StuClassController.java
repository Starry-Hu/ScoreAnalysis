package com.scoreanalysis.controller;

import com.scoreanalysis.bean.StuClass;
import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.service.StuClassService;
import com.scoreanalysis.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Project scoreanalysis
 * @ClassName StuClassController
 * @Author StarryHu
 * @Description 班级相关接口
 * @Date 2019/3/26 20:58
 */
@RequestMapping("class")
@RestController
public class StuClassController extends BaseController{
    @Autowired
    private StuClassService stuClassService;
    
    /** 
    * @Description: 获取全部班级列表
    * @Param: [] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/26 
    */ 
    @GetMapping("/getAllClasses")
    public BaseResponse getAllClasses() throws Exception{
        List<StuClass> data = stuClassService.getAllClasses();
        
        return ajaxSucc(data, ResultEnum.CLASS_SEARCH_SUCCESS);
    }
}
