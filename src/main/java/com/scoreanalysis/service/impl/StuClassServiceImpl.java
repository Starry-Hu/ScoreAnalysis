package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.StuClass;
import com.scoreanalysis.bean.StuClassExample;
import com.scoreanalysis.dao.StuClassMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.StuClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Project scoreanalysis
 * @ClassName StuClassServiceImpl
 * @Author StarryHu
 * @Description 班级相关的业务逻辑层
 * @Date 2019/3/26 21:01
 */
@Service("stuClassService")
public class StuClassServiceImpl implements StuClassService{
    @Autowired
    private StuClassMapper stuClassMapper;

    /**
    * @Description: 获取全部班级列表
    * @Param: []
    * @return: java.util.List<com.scoreanalysis.bean.StuClass>
    * @Author: StarryHu
    * @Date: 2019/3/26
    */
    public List<StuClass> getAllClasses() throws Exception {
        // 查找所有班级id不为空的（全部班级）
        StuClassExample stuClassExample = new StuClassExample();
        stuClassExample.createCriteria().andClsidIsNotNull();

        List<StuClass> stuClassList = stuClassMapper.selectByExample(stuClassExample);
        if (stuClassList.size() == 0){
            throw new SAException(ExceptionEnum.CLASS_NO_EXIST);
        }

        return stuClassList;
    }
}
