package com.scoreanalysis.enums;

/**
 * @EnumName ExceptionEnum
 * @Description 异常信息的枚举接口
 * @author yang_
 * @Date 2018/11/4 18:32
 */
public enum ExceptionEnum {
    // 上传文件相关
    UPLOAD_FORM_ERROR(101,"上传文件格式不正确"),
    UPLOAD_EMPTY(102,"文件为空"),

    // 管理员相关
    ADMIN_NO_EXIST(122,"管理员不存在"),
    ADMIN_ADD_FAIL(123,"管理员添加失败"),
    ADMIN_DELETE_FAIL(124,"管理员删除失败"),
    ADMIN_UPDATE_FAIL(125,"管理员更新失败"),
    ADMIN_SEARCH_FAIL(126,"管理员查找失败"),

    // 教学计划相关
    PLAN_EXIST(221,"教学计划已存在"),
    PLAN_NO_EXIST(222,"教学计划不存在"),
    PLAN_ADD_FAIL(223,"教学计划添加失败"),
    PLAN_DELETE_FAIL(224,"教学计划删除失败"),
    PLAN_UPDATE_FAIL(225,"教学计划更新失败"),
    PLAN_SEARCH_FAIL(226,"教学计划查找失败"),

    PLAN_DATA_DELETE_SUCC(227,"教学计划相关数据删除成功！"),
    PLAN_DATA_DELETE_FAIL(228,"教学计划相关数据删除失败！"),
    PLAN_DATA_EMPTY(229,"教学计划相关数据为空！"),

    // 学生相关
    STUDENT_EXIST(101,"学生已经存在"),
    STUDENT__NO_EXIST(102,"学生不存在"),
    STUDENT_ADD_FAIL(223,"学生添加失败"),
    STUDENT_DELETE_FAIL(224,"学生删除失败"),
    STUDENT_UPDATE_FAIL(225,"学生更新失败"),
    STUDENT_SEARCH_FAIL(226,"学生查找失败"),

    STUDENT_DATA_DELETE_SUCC(227,"学生相关数据删除成功！"),
    STUDENT_DATA_DELETE_FAIL(228,"学生相关数据删除失败！"),
    STUDENT_DATA_EMPTY(229,"学生相关数据为空！"),

    // 课程相关
    COURSE_EXIST(221,"课程已存在"),
    COURSE_NO_EXIST(222,"课程不存在"),
    COURSE_ADD_FAIL(223,"课程添加失败"),
    COURSE_DELETE_FAIL(224,"课程删除失败"),
    COURSE_UPDATE_FAIL(225,"课程更新失败"),
    COURSE_SEARCH_FAIL(226,"课程查找失败"),

    // 专业相关
    MAJOR_EXIST(221,"专业已存在"),
    MAJOR_NO_EXIST(222,"专业不存在"),
    MAJOR_ADD_FAIL(223,"专业添加失败"),
    MAJOR_DELETE_FAIL(224,"专业删除失败"),
    MAJOR_UPDATE_FAIL(225,"专业更新失败"),
    MAJOR_SEARCH_FAIL(226,"专业查找失败"),

    // 教学计划与课程关系相关
    PLAN_COURSE_EXIST(221,"已存在"),
    PLAN_COURSE_NO_EXIST(222,"教学计划不存在"),
    PLAN_COURSE_ADD_FAIL(223,"教学计划添加失败"),
    PLAN_COURSE_DELETE_FAIL(224,"教学计划删除失败"),
    PLAN_COURSE_UPDATE_FAIL(225,"教学计划更新失败"),
    PLAN_COURSE_SEARCH_FAIL(226,"教学计划查找失败"),

    // 班级相关
    CLASS_EXIST(101,"班级已经存在"),
    CLASS_NO_EXIST(102,"班级不存在"),
    CLASS_ADD_FAIL(223,"班级添加失败"),
    CLASS_DELETE_FAIL(224,"班级删除失败"),
    CLASS_UPDATE_FAIL(225,"班级更新失败"),
    CLASS_SEARCH_FAIL(226,"班级查找失败"),

    // 学生课程信息相关
    STU_COURSE_EXIST(101,"学生课程信息已经存在"),
    STU_COURSE_NO_EXIST(102,"学生课程信息不存在"),
    STU_COURSE_ADD_FAIL(223,"学生课程信息添加失败"),
    STU_COURSE_DELETE_FAIL(224,"学生课程信息删除失败"),
    STU_COURSE_UPDATE_FAIL(225,"学生课程信息更新失败"),
    STU_COURSE_SEARCH_FAIL(226,"学生课程信息查找失败"),

    // 通知相关
    STU_INFORM_NULL(101,"学生联系方式不存在"),
    STU_EMAIL_INFORM_NULL(102,"学生邮箱联系方式不存在"),
    STU_PHONE_INFORM_NULL(103,"学生电话联系方式不存在"),
    ;

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg){
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
