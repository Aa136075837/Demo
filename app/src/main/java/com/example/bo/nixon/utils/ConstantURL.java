package com.example.bo.nixon.utils;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public class ConstantURL {
    private static final String OUTER_NET = "http://helppage8dmap.chinacloudapp.cn:8080/nixon/";

    private static final String INNER_NET = "http://192.168.0.150:8080/com.dmap.nixon/";

    private static final String BASE_URL = OUTER_NET;

    public static final String LOGIN_URL = BASE_URL + "customer/account/login";

    public static final String REGISTE_URL = BASE_URL + "customer/account/regist";

    public static final String UPDATE_USER_INFO = BASE_URL + "customer/csinfo/updateCsInfo";

    public static final String GET_USER_INFO = BASE_URL + "customer/csinfo/getCsInfo";

    public static final String SEND_ACTIVATE_LINK = BASE_URL + "customer/account/sendActivateLink";

    public static final String SEND_RESET_PSD_CODE = BASE_URL + "customer/account/sendResetPwdCode";

    public static final String RESET_PASSWORD = BASE_URL + "customer/account/resetPwd";

    public static final String RESET_CHECK_CODE = BASE_URL + "customer/account/checkCode";

    public static final String UPDATE_STEPS = BASE_URL + "customer/device/updateSteps";

    public static final String BIND_DEVICE = BASE_URL + "customer/device/bindDevice";

    public static final String GET_STEP_BY_DAY = BASE_URL + "customer/device/getStepsByDays";

    public static final String GET_STEP_BY_WEEK = BASE_URL + "customer/device/getStepsByWeeks";

    public static final String GET_STEP_BY_MONTHS = BASE_URL + "customer/device/getStepsByMonths";

    public static final String GET_CITY_LIST = BASE_URL + "customer/extension/getTimezoneList";

    public static final String LOG_OUT = BASE_URL + "customer/account/logout";

    public static final String ADD_FAVORITE_CONTACT = BASE_URL + "customer/extension/addLinkmans";

    public static final String DEL_FAVORITE_CONTACT = BASE_URL +
            "customer/extension/deleteLinkmans";

    public static final String GET_FAVORITE_CONTACT = BASE_URL + "customer/extension/getLinkmans";

    public static final String UPDATE_FAVORITE_CONTACT = BASE_URL +
            "customer/extension/updateLinkman";

    public static final String LOGIN_GOOGLE = BASE_URL + "customer/account/oauth2Login";

    public static final String ADD_ALARM = BASE_URL + "customer/extension/addAlarm";

    public static final String GET_ALARM = BASE_URL + "customer/extension/getAlarms";

    public static final String UPDATE_ALARM = BASE_URL + "customer/extension/updateAlarm";

    public static final String DEL_ALARM = BASE_URL + "customer/extension/deleteAlarms";

    public static final String ADD_ALARMS = BASE_URL + "customer/extension/addAlarms";

    public static final String UPDATE_SECOND_TIMEZONE = BASE_URL + "extension/updateSecondTimezone";

    public static final String UPDATE_LIST_STEPS = BASE_URL + "customer/device/uploadSteps";

    public static final String NIXON_COM_URL = "http://www.nixon.com/smart";
}
