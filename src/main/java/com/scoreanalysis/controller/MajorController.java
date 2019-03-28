package com.scoreanalysis.controller;

import com.scoreanalysis.bean.Major;
import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.service.MajorService;
import com.scoreanalysis.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Project scoreanalysis
 * @ClassName MajorController
 * @Author StarryHu
 * @Description 专业相关接口
 * @Date 2019/3/27 21:30
 */
@RestController
@RequestMapping("major")
public class MajorController extends BaseController {
    @Autowired
    private MajorService majorService;
    
    /** 
    * @Description: 获取全部专业列表
    * @Param: [] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/27 
    */ 
    @GetMapping("/getAllMajors")
    public BaseResponse getAllMajors() throws Exception{
        List<Major> majorList = majorService.getAllMajors();

        return ajaxSucc(majorList, ResultEnum.MAJOR_SEARCH_SUCCESS);
    }
}
