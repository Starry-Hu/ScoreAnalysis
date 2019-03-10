package com.scoreanalysis.service;

import com.scoreanalysis.bean.Admin;

public interface AdminService {

    int addAdmin(String adminId, String name, String psw) throws Exception;

    int deleteAdminTotal(String id) throws Exception;

    int deleteAdminLogic(String id) throws Exception;

    int updateAdmin(String id, String name, String psw) throws Exception;

    Admin getAdminByuuid(String id) throws Exception;

    Admin getAdminByAdminId(String AdminId) throws Exception;

    // 为内部所用的查找管理员
    Admin findAdminByAdminId(String adminId);
}