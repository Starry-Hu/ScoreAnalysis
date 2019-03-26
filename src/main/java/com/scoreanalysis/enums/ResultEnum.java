package com.scoreanalysis.enums;

/**
 * @EnumName ResultEnum
 * @Author yang_
 * @Description 请求接口的枚举信息
 * @Date 2018/11/4 18:04
 */
public enum  ResultEnum {
    UNKNOW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    FAIL(1,"失败"),

    // 上传excel表格相关
    EXCEL_FORM_ERROR(401,"上传文件格式不正确"),
    
    // 管理员相关
    ADMIN_LOGIN_SUCCESS(100,"登陆成功"),
    ADMIN_LOGIN_FAIL(101,"登陆失败"),
    ADMIN_EXSIT(102,"管理员已存在"),
    ADMIN_ADD_SUCCESS(103,"管理员添加成功"),
    ADMIN_DELETE_SUCCESS(104,"管理员删除成功"),
    ADMIN_UPDATE_SUCCESS(105,"管理员更新成功"),
    ADMIN_SEARCH_SUCCESS(106,"管理员查找成功"),
    ADMIN_INFO_NOT_FULL(111,"信息填写不完全"),
    ADMIN_PSW_ERROR(112,"账户或密码错误"),

    // 教学计划相关
    PLAN_EXIST(201,"教学计划已存在"),
    PLAN_ADD_SUCCESS(202,"教学计划添加成功"),
    PLAN_DELETE_SUCCESS(203,"教学计划删除成功"),
    PLAN_UPDATE_SUCCESS(204,"教学计划更新成功"),
    PLAN_SEARCH_SUCCESS(205,"教学计划查找成功"),
    PLAN_INFO_NOT_FULL(211,"信息填写不完全"),

    PLAN_DATA_ADD_SUCC(226,"学生相关数据添加成功"),
    PLAN_DATA_DELETE_SUCC(227,"教学计划相关数据删除成功！"),


    // 学生信息相关
    STUDENT_EXIST(201,"学生信息已存在"),
    STUDENT_ADD_SUCCESS(202,"学生信息添加成功"),
    STUDENT_DELETE_SUCCESS(203,"学生信息删除成功"),
    STUDENT_UPDATE_SUCCESS(204,"学生信息更新成功"),
    STUDENT_SEARCH_SUCCESS(205,"学生信息查找成功"),
    STUDENT_INFO_NOT_FULL(211,"信息填写不完全"),

    STUDENT_DATA_ADD_SUCC(226,"学生相关数据添加成功"),
    STUDENT_DATA_DELETE_SUCC(227,"学生相关数据删除成功！"),


    // 课程相关
    COURSE_EXIST(201,"课程已存在"),
    COURSE_ADD_SUCCESS(202,"课程添加成功"),
    COURSE_DELETE_SUCCESS(203,"课程删除成功"),
    COURSE_UPDATE_SUCCESS(204,"课程更新成功"),
    COURSE_SEARCH_SUCCESS(205,"课程查找成功"),
    COURSE_INFO_NOT_FULL(211,"信息填写不完全"),

    // 专业相关
    MAJOR_EXIST(201,"专业已存在"),
    MAJOR_ADD_SUCCESS(202,"专业添加成功"),
    MAJOR_DELETE_SUCCESS(203,"专业删除成功"),
    MAJOR_UPDATE_SUCCESS(204,"专业更新成功"),
    MAJOR_SEARCH_SUCCESS(205,"专业查找成功"),
    MAJOR_INFO_NOT_FULL(211,"信息填写不完全"),

    // 班级相关
    CLASS_EXIST(201,"班级已存在"),
    CLASS_ADD_SUCCESS(202,"班级添加成功"),
    CLASS_DELETE_SUCCESS(203,"班级删除成功"),
    CLASS_UPDATE_SUCCESS(204,"班级更新成功"),
    CLASS_SEARCH_SUCCESS(205,"班级查找成功"),
    CLASS_INFO_NOT_FULL(211,"信息填写不完全"),
    ;
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
