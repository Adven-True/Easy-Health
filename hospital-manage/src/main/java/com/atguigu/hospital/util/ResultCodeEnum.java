package com.atguigu.hospital.util;
import lombok.Getter;

/**
 * Global Unified Return Result Class
 *
 * @author xy
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"Success"),
    FAIL(201, "Fail"),
    SERVICE_ERROR(202, "Service Exception"),
    DATA_ERROR(204, "Data Exception"),

    SIGN_ERROR(300, "Signature Error"),

    PAY_PASSWORD_ERROR(401, "Payment Password Error"),
    REPEAT_ERROR(402, "Duplicate Submission"),

    INVEST_AMMOUNT_MORE_ERROR(501, "The lending amount exceeds the target amount"),
    RETURN_AMMOUNT_MORE_ERROR(502, "The repayment amount is incorrect"),
    PROJECT_AMMOUNT_ERROR(503, "Inconsistent target amounts")
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
