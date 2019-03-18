package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.Major;
import com.scoreanalysis.dao.MajorMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.MajorService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


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
        try {
            int n = majorMapper.deleteByPrimaryKey(mid);
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
        if (major == null){
            throw new SAException(ExceptionEnum.MAJOR_NO_EXIST);
        }return major;
    };

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean batchUpload(String fileName, MultipartFile file, boolean isExcel2003) throws Exception{
        boolean notNull = false;

        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            notNull = true;
        }

        return false;
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
        if (major == null){
            return null;
        }return major;
    }
}
