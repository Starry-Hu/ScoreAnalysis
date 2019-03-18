package com.scoreanalysis.service;

import com.scoreanalysis.bean.Major;
import org.springframework.web.multipart.MultipartFile;

public interface MajorService {
    int addMajor(String mid,String mname,String mplan) throws Exception;

    int deleteMajor(String mid) throws Exception;

    int updateMajor(String mid,String mname,String mplan) throws Exception;

    Major getMajorById(String mid) throws Exception;

    boolean batchUpload(String fileName, MultipartFile file, boolean isExcel2003) throws Exception;

    Major findMajorById(String mid);

}
