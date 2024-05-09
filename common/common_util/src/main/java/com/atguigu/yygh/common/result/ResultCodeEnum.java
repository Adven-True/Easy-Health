package com.atguigu.yygh.common.result;

import lombok.Getter;

/**
 * Unified Return Result Status Information Class
 * @author Administrator
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"Success"),
    FAIL(201, "Fail"),
    PARAM_ERROR( 202, "Incorrect Param"),
    SERVICE_ERROR(203, "Service Exception"),
    DATA_ERROR(204, "Data Exception"),
    DATA_UPDATE_ERROR(205, "Data Version Exception"),

    LOGIN_AUTH(208, "Not Logged In"),
    PERMISSION(209, "Unauthorized"),

    CODE_ERROR(210, "Incorrect Captcha"),
//    LOGIN_MOBLE_ERROR(211, "Incorrect account"),
    LOGIN_DISABLED_ERROR(212, "This user has been disabled"),
    REGISTER_MOBLE_ERROR(213, "Phone number already in use"),
    LOGIN_AURH(214, "Login Required"),
    LOGIN_ACL(215, "Unauthorized"),

    URL_ENCODE_ERROR( 216, "URL encoding failed"),
    ILLEGAL_CALLBACK_REQUEST_ERROR( 217, "Illegal Callback Request"),
    FETCH_ACCESSTOKEN_FAILD( 218, "Failed to obtain accessToken"),
    FETCH_USERINFO_ERROR( 219, "Failed to retrieve user information"),
    //LOGIN_ERROR( 23005, "Login failed"),

    PAY_RUN(220, "Payment in progress"),
    CANCEL_ORDER_FAIL(225, "Failed to cancel the order"),
    CANCEL_ORDER_NO(225, "Cannot cancel appointment"),

    HOSCODE_EXIST(230, "Hospital code already exists"),
    NUMBER_NO(240, "Insufficient available appointments"),
    TIME_NO(250, "Booking is not available at the current time"),

    SIGN_ERROR(300, "Signature error"),
    HOSPITAL_OPEN(310, "The hospital is not yet open. Access is temporarily unavailable"),
    HOSPITAL_LOCK(320, "The hospital is locked and temporarily inaccessible"),
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
