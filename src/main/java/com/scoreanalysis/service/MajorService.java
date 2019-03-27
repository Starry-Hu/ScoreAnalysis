package com.scoreanalysis.service;

import com.scoreanalysis.bean.Major;

import java.util.List;

public interface MajorService {
    // 获取全部专业列表
    List<Major> getAllMajors() throws Exception;
}
