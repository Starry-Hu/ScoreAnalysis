package com.scoreanalysis.controller;

import com.scoreanalysis.bean.Major;
import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.service.MajorService;
import com.scoreanalysis.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project scoreanalysis
 * @ClassName MajorController
 * @Author StarryHu
 * @Description 专业相关接口
 * @Date 2019/3/5 18:29
 */

@RestController
@RequestMapping("major")
public class MajorController extends BaseController {
    @Autowired
    private MajorService majorService;

    /** 
    * @Description: 添加专业信息 
    * @Param: [mid, mname, mplan] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    @PostMapping("/add")
    public BaseResponse addMajor(String mid,String mname,String mplan) throws Exception{
        // 检查信息是否填写完全
        if (mid == null || mname == null || mplan == null 
                || mid.trim().equals("") || mname.trim().equals("") || mplan.trim().equals("")){
            return ajaxFail(ResultEnum.MAJOR_INFO_NOT_FULL);
        }
        // 检查内部是否有同id专业  采用内部查找 无异常的抛出
        Major major = majorService.findMajorById(mid);
        if (major != null){
            // 已存在情况
            return ajaxFail(ResultEnum.MAJOR_EXIST);
        }
        // 添加专业信息
        majorService.addMajor(mid,mname,mplan);
        return ajaxSucc(null,ResultEnum.MAJOR_ADD_SUCCESS);
    }
    
    /** 
    * @Description: 删除专业信息 
    * @Param: [mid] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    @GetMapping("/delete")
    public BaseResponse deleteMajor(String mid) throws Exception{
        // 检查信息是否填写完全
        if (mid == null || mid.trim().equals("") ){
            return ajaxFail(ResultEnum.MAJOR_INFO_NOT_FULL);
        }
        // 查找对应id的专业
        Major major = majorService.getMajorById(mid);
        // 删除专业
        majorService.deleteMajor(mid);
        return ajaxSucc(null,ResultEnum.MAJOR_DELETE_SUCCESS);
    }
    
    /** 
    * @Description: 更新专业信息
    * @Param: [mid, mname, mplan] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    @PostMapping("/update")
    public BaseResponse updateMajor(String mid,String mname,String mplan) throws Exception{
        // 检查信息是否填写完全
        if (mid == null || mname == null || mplan == null
                || mid.trim().equals("") || mname.trim().equals("") || mplan.trim().equals("")){
            return ajaxFail(ResultEnum.MAJOR_INFO_NOT_FULL);
        }
        // 查找该id的专业信息
        Major major = majorService.getMajorById(mid);
        // 添加专业信息
        majorService.updateMajor(mid,mname,mplan);
        return ajaxSucc(null,ResultEnum.MAJOR_UPDATE_SUCCESS);
    }
}
