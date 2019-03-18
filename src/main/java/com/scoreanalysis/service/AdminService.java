package com.scoreanalysis.service;

import com.scoreanalysis.bean.Admin;

public interface AdminService {

    int addAdmin(String adminId, String name, String psw) throws Exception;

    int deleteAdminLogic(String uuid) throws Exception;

    int updateAdmin(String uuid, String name, String psw) throws Exception;

    Admin getAdminByuuid(String uuid) throws Exception;

    Admin getAdminByAdminId(String AdminId) throws Exception;

    // 为内部所用的查找管理员
    Admin findAdminByAdminId(String adminId);
}