package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.Major;
import com.scoreanalysis.bean.MajorExample;
import com.scoreanalysis.dao.MajorMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Project scoreanalysis
 * @ClassName MajorServiceImpl
 * @Author StarryHu
 * @Description 专业的业务逻辑层
 * @Date 2019/3/27 21:27
 */
@Service("majorService")
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorMapper majorMapper;

    /**
    * @Description: 获取全部专业列表
    * @Param: []
    * @return: java.util.List<com.scoreanalysis.bean.Major>
    * @Author: StarryHu
    * @Date: 2019/3/27
    */
    public List<Major> getAllMajors() throws Exception{
        // 获取全部专业（专业id不为空）
        MajorExample majorExample = new MajorExample();
        majorExample.createCriteria().andMidIsNotNull();
        List<Major> majorList = majorMapper.selectByExample(majorExample);

        // 如果列表为空，则抛异常
        if (majorList.size() == 0){
            throw new SAException(ExceptionEnum.MAJOR_NO_EXIST);
        }
        return majorList;
    }
}
