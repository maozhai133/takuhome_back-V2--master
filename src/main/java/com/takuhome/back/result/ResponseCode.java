package com.takuhome.back.result;

/**
 * @author nekotaku
 * @create 2021-08-07 18:54
 */
public enum ResponseCode {

    //公共请求信息
    SUCCESS(200,"请求成功！"),
    TABLE_SUCCESS(0,"请求成功！"),
    FAIL(500,"请求失败,请联系管理员！"),
    PARAMETER_MISSING(600,"参数缺失！"),
    UNAUTHORIZED(401,"未授权！"),

    //分类信息
    //5000100 - 5000200
    CATEGORY_NAME_REPEAT(5000100,"分类名已存在，请重新输入！"),
    CATEGORY_PARENT_ZERO(5000101,"当前分类为父级分类，并包含有子级分类，请先清空子分类再删除！"),
    CATEGORY_ARTICLE_CONTAIN(500102,"当前分类在博文中存在，请先删除或修改相关博文分类！"),
    CATEGORY_ARTICLE_TOP(500102,"当前分类为父级分类，并包含有子级分类，请先清空子分类再修改！"),

    //标签信息
    //5000201-5000300
    TAG_NAME_REPEAT(500200,"标签名已存在，请重新输入！"),
    TAG_ARTICLE_CONTAIN(500201,"当前标签在博文中存在，请先删除或修改相关博文标签！"),

    //上传文件信息
    //5000301-5000400
    FILE_EMPTY(500300,"上传文件为空，请选择上传文件！"),

    //博文信息
    //5000401-5000500
    ARTICLE_TITLE_REPEAT(5000400,"博文标题已存在，请重新命名博文标题！"),

    //用户信息
    //5000501-5000600
    USERNAME_REPEAT(5000500,"用户名不可用，请更换！"),
    EMAIL_REPEAT(5000501,"邮箱已存在！"),
    LOGIN_ERROR(5000502,"会话超时，请刷新页面！"),
    LOGIN_CPACHA(5000503,"验证码错误，请输入正确的验证码!"),
    USERNAME_NULL(5000504,"用户名或密码错误，请重新输入！"),
    PASSWORD_ERROR(5000505,"用户名或密码错误，请重新输入！"),
    CODE_TIMEOUT(5000506,"邮箱验证码失效，请重新发送！"),
    CODE_VOIDVALUE(500507,"你还没有发送验证码！"),
    NICKNAME_REPEAT(500508,"用户昵称已存在！"),
    ORIGINAL_PASSWORD(500509,"原密码错误，请输入正确的原密码！"),
    EMAIL_ILLEGALITY(500510,"非当前登录用户绑定邮箱，请输入绑定的邮箱！"),
    PASSWORD_REPEAT(500511,"新密码和原密码重复，请输入新的密码！"),
    USER_PERMISSION_DENIED(50512,"当前账户已被停用或没有权限，请联系管理员！"),
    EMAIL_ERROR(5000513,"当前邮箱账户不存在，请重新输入！");


    private Integer code;
    private String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getMessage(String name){
        for(ResponseCode item:ResponseCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return null;
    }

    public static Integer getCode(String name){
        for(ResponseCode item:ResponseCode.values()){
            if(item.name().equals(name)){
                return item.code;
            }
        }
        return null;
    }


}
