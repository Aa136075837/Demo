package com.example.bo.nixon.utils;

public class RequestCode {
    /**
     * 成功
     */
    public static final String SUCCESS = "00000000";
    /**
     * 手机格式错误
     */
    public static final String WRONG_PHONE_NUMBER = "10050008";
    /**
     * 操作过于频繁
     */
    public static final String REQUEST_TOO_OFTEN = "10050006";
    /**
     * 验证码发送失败
     */
    public static final String CODE_SEND_FAILED = "10050006";
    /**
     * 验证码校验错误
     */
    public static final String CODE_CHECK_WRONG = "10050005";
    /**
     * 参数不合法
     */
    public static final String PARAMETER_WRONG = "10020006";
    /**
     * 两次密码不一致
     */
    public static final String PASSWORD_NO_SAME = "10040002";
    /**
     * 手机号和验证码未经过验证
     */
    public static final String NOT_CHECKED_BOTH = "10020009";
    /**
     * 注册提交的手机号/邮箱与验证时不一致
     */
    public static final String NOT_CHECKED_USERNAME = "10020007";
    /**
     * 注册提交的验证码与验证时不一致
     */
    public static final String NOT_CHECKED_CODE = "10020008";
    /**
     * 密码规则不合法 (以字母开头，长度在8~18之间，只能包含字符、数字和下划线)
     */
    public static final String PASSWORD_NOT_RULE = "10020005";
    /**
     * 用户名长度规则不合法
     */
    public static final String USERNAME_LENGTH_NO_RULE = "10020004";
    /**
     * 用户已存在
     */
    public static final String USERNAME_ALREADY_EXIST = "10020002";
    /**
     * 添加用户失败或者此用户不存在（重置密码时检验）
     */
    public static final String INSERT_FAILED = "10020001";
    /**
     * 用户名密码错误
     */
    public static final String USERNAME_PASS_WRONG = "10050002";
    /**
     * 用户不存在
     */
    public static final String USER_NOT_EXIST = "10050001";
    /**
     * 用户已锁定
     */
    public static final String USER_LOCKED = "10010002";
    /**
     * 用户未登录,无法进行退出操作
     */
    public static final String SIGN_OUT_FAILED = "10050009";
    /**
     * 目标用户不存在 根据手机/邮箱查找用户查不到时抛此异常目标用户不存在 根据手机/邮箱查找用户查不到时抛此异常
     */
    public static final String RESET_PASS_FAILED = "10030002";
    /**
     * 发送验证码错误,目标服务器响应错误
     */
    public static final String CHECK_CODE_SEND_FAILED = "10050010";
    /**
     * 检测到有可用的验证码,请勿重复请求
     */
    public static final String CHECK_CODE_ALREADY_SEND = "10050015";
    /**
     * 没有有可用的验证码,请再次提交请求
     */
    public static final String CHECK_CODE_EXPIRED = "10050016";
    /**
     * 暂不受理该区域手机号码短信业务
     */
    public static final String NOT_SUPPORT_AREA = "10050017";
    /**
     * 邮箱格式不符合规则：不符合邮箱格式的数据
     */
    public static final String ERROR_EMAIL = "10020010";
    /**
     * 邮件发送失败：其他原因产生的
     */
    public static final String SEND_EMAIL_FAILED = "10050020";
    /**
     * session已失效,请清除cookie重新登陆 PS.后台缓存中找不到请求携带cookie中的信息,需要重新登陆
     */
    public static final String SECCION_FAILURE = "10060003";
    /**
     * 未登录
     */
    public static final String NOT_LOGIN_STATE = "10060002";
    /**
     * 判断用户七天未激活时会被强制退出并返回该错误
     */
    public static final String USER_NO_ACTIVATED = "10010003";
    /**
     * 无数据:传入的参数为空时
     */
    public static final String UPDATE_NULL_INFO = "00990005";
    /**
     * 上传数据超范围:当上传的值超出规定范围时
     */
    public static final String OUT_OF_RANGE = "00990011";
    /**
     * 设备已经绑定
     */
    public static final String DEVICE_BINDED = "11010001";
    /**
     * 缺少用户信息
     */
    public static final String LACK_USERINFO = "11010004";
}