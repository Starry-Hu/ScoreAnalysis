package com.scoreanalysis.service.impl;

import com.scoreanalysis.bean.Admin;
import com.scoreanalysis.bean.AdminExample;
import com.scoreanalysis.dao.AdminMapper;
import com.scoreanalysis.enums.ExceptionEnum;
import com.scoreanalysis.exception.SAException;
import com.scoreanalysis.service.AdminService;
import com.scoreanalysis.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Project scoreanalysis
 * @ClassName AdminServiceImpl
 * @Author 葫芦胡
 * @Description 管理员相关业务逻辑层
 * @Date 2019/2/9 19:20
 */

@Service("adminService")
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    /**
     * @Description: 添加管理员
     * @Param: [adminId, name, psw]
     * @return: int
     * @Author: 葫芦胡
     * @Date: 2019/2/9
     */
    public int addAdmin(String adminId, String name, String psw) throws Exception {
        Admin admin = new Admin();
        admin.setId(IDGenerator.generator());
        admin.setAdminId(adminId);
        admin.setName(name);
        admin.setPsw(psw);
        admin.setIsdel(0);

        try {
            int n = adminMapper.insert(admin);
            // 添加成功
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.ADMIN_ADD_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 根据uuid删除管理员  逻辑删除
     * @Param: [id]
     * @return: int
     * @Author: 葫芦胡
     * @Date: 2019/2/9
     */
    public int deleteAdminLogic(String uuid) throws Exception {
        Admin admin = new Admin();
        admin.setId(uuid);
        admin.setIsdel(1);
        try {
            int n = adminMapper.updateByPrimaryKey(admin);
            // 删除成功
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.ADMIN_DELETE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 根据uuid更新账号
     * @Param: [id, name, psw]
     * @return: int
     * @Author: 葫芦胡
     * @Date: 2019/2/9
     */
    public int updateAdmin(String uuid, String name, String psw) throws Exception {
        Admin admin = new Admin();
        admin.setId(uuid);
        admin.setName(name);
        admin.setPsw(psw);

        try {
            int n = adminMapper.updateByPrimaryKey(admin);
            if (n > 0) {
                return n;
            }
            throw new SAException(ExceptionEnum.ADMIN_UPDATE_FAIL);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 根据uuid管理员id查找相应的管理员
     * @Param: [id]
     * @return: com.scoreanalysis.bean.Admin
     * @Author: 葫芦胡
     * @Date: 2019/2/9
     */
    public Admin getAdminByuuid(String id) throws Exception {
        try {
            Admin admin = adminMapper.selectByPrimaryKey(id);
            // 管理员不存在
            if (admin == null) {
                throw new SAException(ExceptionEnum.ADMIN_NO_EXIST);
            }
            return admin;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Description: 根据管理员账号id查找管理员 (带异常的抛出)
     * @Param: [adminId]
     * @return: com.scoreanalysis.bean.Admin
     * @Author: 葫芦胡
     * @Date: 2019/2/9
     */
    public Admin getAdminByAdminId(String adminId) throws Exception {
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andAdminIdEqualTo(adminId);
        try {
            List<Admin> datas = adminMapper.selectByExample(adminExample);
            // 查出来整个数据是空的  管理员不存在
            if (datas.isEmpty()) {
                throw new SAException(ExceptionEnum.ADMIN_NO_EXIST);
            }
            // 若不空 则取第一条判断
            Admin admin = datas.get(0);
            if (admin == null || admin.getIsdel() == 1) {
                throw new SAException(ExceptionEnum.ADMIN_NO_EXIST);
            }
            return admin;
        } catch (Exception e) {
            throw e;
        }
    }






    /**
     * @Description: 根据管理员账号查找管理员（不抛出异常  为内部所用）
     * @Param: [adminId]
     * @return: com.scoreanalysis.bean.Admin
     * @Author: 葫芦胡
     * @Date: 2019/2/9
     */
    public Admin findAdminByAdminId(String adminId) {
        AdminExample adminexample = new AdminExample();
        adminexample.createCriteria().andAdminIdEqualTo(adminId);
        List<Admin> datas = adminMapper.selectByExample(adminexample);
        // 查出来整个数据是空的  管理员不存在
        if (datas.isEmpty()) {
            return null;
        }
        // 若不空 则取第一条判断
        Admin admin = datas.get(0);
        if (admin == null || admin.getIsdel() == 1) {
            return null;
        }
        return admin;
    }

}
