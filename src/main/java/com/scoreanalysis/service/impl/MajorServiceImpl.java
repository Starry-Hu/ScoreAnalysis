package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.Major;
import com.scoreanalysis.dao.MajorMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project scoreanalysis
 * @ClassName MajorServiceImpl
 * @Author StarryHu
 * @Description 专业的业务逻辑层
 * @Date 2019/3/5 18:08
 */
@Service("majorService")
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorMapper majorMapper;

    /**
    * @Description:  添加专业
    * @Param: [mid, mname, mplan]
    * @return: int
    * @Author: StarryHu
    * @Date: 2019/3/5
    */
    public int addMajor(String mid,String mname,String mplan) throws Exception{
        Major major = new Major();
        major.setMid(mid);
        major.setMname(mname);
        major.setMplan(mplan);
        major.setIsdel(0);

        try {
            int n = majorMapper.insert(major);
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.MAJOR_ADD_FAIL);
        }catch (Exception e){
            throw e;
        }
    };

    /** 
    * @Description: 删除专业信息 
    * @Param: [mid] 
    * @return: int 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    public int deleteMajor(String mid) throws Exception{
        Major major = new Major();
        major.setMid(mid);
        major.setIsdel(1);
        try {
            int n = majorMapper.updateByPrimaryKeySelective(major);
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.MAJOR_DELETE_FAIL);
        }catch (Exception e){
            throw e;
        }
    };

    /** 
    * @Description: 更新专业信息
    * @Param: [mid, mname, mplan] 
    * @return: int 
    * @Author: StarryHu
    * @Date: 2019/3/5 
    */ 
    public int updateMajor(String mid,String mname,String mplan) throws Exception{
        Major major = new Major();
        major.setMid(mid);
        major.setMname(mname);
        major.setMplan(mplan);
        
        try {
           int n = majorMapper.updateByPrimaryKeySelective(major);
           if (n > 0){
               return n;
           }throw new SAException(ExceptionEnum.MAJOR_UPDATE_FAIL);
        }catch (Exception e){
            throw e;
        }
    };

    /***
    * @Description: 获取专业信息
    * @Param: [mid]
    * @return: com.scoreanalysis.bean.Major
    * @Author: StarryHu
    * @Date: 2019/3/5
    */
    public Major getMajorById(String mid) throws Exception{
        Major major = majorMapper.selectByPrimaryKey(mid);
        if (major == null || major.getIsdel() == 1){
            throw new SAException(ExceptionEnum.MAJOR_NO_EXIST);
        }return major;
    };





    /**
    * @Description: 查找专业信息  为内部所用  无异常的抛出
    * @Param: [mid]
    * @return: com.scoreanalysis.bean.Major
    * @Author: StarryHu
    * @Date: 2019/3/5
    */
    public Major findMajorById(String mid){
        Major major = majorMapper.selectByPrimaryKey(mid);
        if (major == null || major.getIsdel() == 1){
            return null;
        }return major;
    }
}
