package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.StuClass;
import com.scoreanalysis.dao.StuClassMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.StuClsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project scoreanalysis
 * @ClassName StuClsServiceImpl
 * @Author StarryHu
 * @Description 班级类 对应的业务逻辑层
 * @Date 2019/3/10 17:34
 */
@Service("stuClassService")
public class StuClassServiceImpl implements StuClsService {
    @Autowired
    private StuClassMapper stuClassMapper;

    /**
    * @Description: 添加班级信息
    * @Param: [clsId, clsName, clsMajor]
    * @return: int
    * @Author: StarryHu
    * @Date: 2019/3/10
    */
    @Override
    public int addStuCls(String clsId, String clsName, String clsMajor) throws Exception {
        StuClass stuClass = new StuClass();
        stuClass.setClsid(clsId);
        stuClass.setClsName(clsName);
        stuClass.setClsMajor(clsMajor);

        try {
            int n = stuClassMapper.insert(stuClass);
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.CLASS_ADD_FAIL);
        }catch (Exception e){
            throw e;
        }
    }

    /** 
    * @Description: 删除班级信息
    * @Param: [clsId] 
    * @return: int 
    * @Author: StarryHu
    * @Date: 2019/3/10 
    */ 
    @Override
    public int deleteStuCls(String clsId) throws Exception {
        try {
            int n = stuClassMapper.deleteByPrimaryKey(clsId);
        }catch (Exception e){
            throw e;
        }
        return 0;
    }

    /** 
    * @Description: 更新班级信息
    * @Param: [clsId, clsName, clsMajor] 
    * @return: int 
    * @Author: StarryHu
    * @Date: 2019/3/10 
    */ 
    @Override
    public int updateStuCls(String clsId, String clsName, String clsMajor) throws Exception {
        StuClass stuClass = new StuClass();
        stuClass.setClsid(clsId);
        stuClass.setClsName(clsName);
        stuClass.setClsMajor(clsMajor);

        try {
            int n = stuClassMapper.updateByPrimaryKeySelective(stuClass);
            if (n > 0){
                return n;
            }throw new SAException(ExceptionEnum.CLASS_UPDATE_FAIL);
        }catch (Exception e){
            throw e;
        }
    }
}
