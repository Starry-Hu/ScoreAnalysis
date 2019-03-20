package com.scoreanalysis.controller;

import com.scoreanalysis.bean.Admin;
import com.scoreanalysis.enums.ResultEnum;
import com.scoreanalysis.service.AdminService;
import com.scoreanalysis.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project scoreanalysis
 * @ClassName AdminController
 * @Author 葫芦胡
 * @Description 管理员相关接口
 * @Date 2019/2/8 23:52
 */

@RestController
@RequestMapping("admin")
public class AdminController extends BaseController {
    @Autowired
    private AdminService adminService;

    /**
     * @Description: 管理员登录
     * @Param: [adminId, psw]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: 葫芦胡
     * @Date: 2019/2/9
     */
    @PostMapping("/login")
    public BaseResponse login(String adminId, String psw, HttpSession session) throws Exception {
        // 检查内容是否填写完全
        if (psw == null || adminId == null || psw.trim().equals("") || adminId.trim().equals("")) {
            return ajaxFail(ResultEnum.ADMIN_INFO_NOT_FULL);
        }

        // 获取相应管理员  可能抛出不存在异常
        Admin admin = adminService.getAdminByAdminId(adminId);
        // 密码错误返回相应信息
        if (!psw.equals(admin.getPsw())) {
            return ajaxFail(ResultEnum.ADMIN_PSW_ERROR);
        }
        // 登录成功
        session.setAttribute("adminId", admin.getAdminId());
        session.setAttribute("adminName", admin.getName());
        return ajaxSucc(null, ResultEnum.ADMIN_LOGIN_SUCCESS);
    }

    /**
     * @Description: 获取当前登录的管理员信息
     * @Param: [session]
     * @return: com.scoreanalysis.util.BaseResponse
     * @Author: 葫芦胡
     * @Date: 2019/2/9
     */
    @GetMapping("/getLogined")
    public BaseResponse getLogined(HttpSession session) {
        Map<String, String> resultMap = new HashMap<String, String>();

        String adminId = (String) session.getAttribute("adminId");
        String adminName = (String) session.getAttribute("adminName");

        // 登录不为空时获取
        if (adminName != null) {
            resultMap.put("adminId", adminId);
            resultMap.put("adminName", adminName);
            return ajaxSucc(resultMap, ResultEnum.SUCCESS);
        }
        return ajaxFail(ResultEnum.FAIL);
    }

    /** 
    * @Description: 退出当前登录管理员
    * @Param: [session] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: StarryHu
    * @Date: 2019/3/19 
    */ 
    @GetMapping("/logout")
    public BaseResponse logout(HttpSession session) throws Exception{
        // 清除session
        //session.invalidate();
        session.removeAttribute("aid");
        session.removeAttribute("adminName");
        session.removeAttribute("adminId");
        return ajaxSucc(null, ResultEnum.SUCCESS);
    }

    /** 
    * @Description: 添加管理员
    * @Param: [adminId, name, psw] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: 葫芦胡
    * @Date: 2019/2/9 
    */
    @PostMapping("/add")
    public BaseResponse addAdmin(String adminId, String name, String psw) throws Exception {
        // 检查内容是否填写完全
        if (adminId == null || psw == null || name == null || adminId.trim().equals("")
                || psw.trim().equals("") || name.trim().equals("")) {
            return ajaxFail(ResultEnum.ADMIN_INFO_NOT_FULL);
        }

        // 判断是否存在 此方法无异常的抛出
        Admin test = adminService.findAdminByAdminId(adminId);
        if (test != null) {
            return ajaxFail(ResultEnum.ADMIN_EXSIT);
        }
        adminService.addAdmin(adminId, name, psw);
        return ajaxSucc(null,ResultEnum.ADMIN_ADD_SUCCESS);
    }

    /** 
    * @Description: 根据uuid 管理员id删除管理员（逻辑删除） 
    * @Param: [id] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: 葫芦胡
    * @Date: 2019/2/10 
    */ 
    @GetMapping("/delete")
    public BaseResponse deleteAdmin(String id) throws Exception{
        // 检查内容是否填写完全
        if (id == null || id.trim().equals("")){
            return ajaxFail(ResultEnum.ADMIN_INFO_NOT_FULL);
        }
        // 获取相应管理员  可能抛出不存在异常
        adminService.getAdminByuuid(id);
        // 删除相应管理员
        adminService.deleteAdminLogic(id);

        return ajaxSucc(null,ResultEnum.ADMIN_DELETE_SUCCESS);
    }
    
    /** 
    * @Description: 根据uuid 更改管理员信息
    * @Param: [id, name, psw] 
    * @return: com.scoreanalysis.util.BaseResponse 
    * @Author: 葫芦胡
    * @Date: 2019/2/10 
    */
    @PostMapping("/update")
    public BaseResponse updateAdmin(String id,String name,String psw) throws Exception{
        // 检查内容是否填写完全
        if (id == null || id.trim().equals("")){
            return ajaxFail(ResultEnum.ADMIN_INFO_NOT_FULL);
        }
        
        adminService.updateAdmin(id,name,psw);
        return ajaxSucc(null,ResultEnum.ADMIN_UPDATE_SUCCESS);
    }

    /***
    * @Description: 根据管理员id查找管理员
    * @Param: [id] 管理员id  非uuid
    * @return: com.scoreanalysis.util.BaseResponse
    * @Author: StarryHu
    * @Date: 2019/3/5
    */
   /* @GetMapping("getByAdminId")
    public BaseResponse getAdminById(String id){
        // 检查id是否填写完全
        if (id == null || id.trim().equals("")){
            return ajaxFail(ResultEnum.ADMIN_INFO_NOT_FULL);
        }

        Admin admin = adminService.getAdminByAdminId(id);
    }*/
}
