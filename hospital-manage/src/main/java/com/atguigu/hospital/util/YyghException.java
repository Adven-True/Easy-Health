package com.atguigu.hospital.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Custom Global Exception Class
 *
 * @author xy
 */
@Data
@ApiModel(value = "Custom Global Exception Class")
public class YyghException extends RuntimeException {

    @ApiModelProperty(value = "Exception Status Code")
    private Integer code;

    /**
     * Create exception objects through status codes and error messages
     * @param message
     * @param code
     */
    public YyghException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public YyghException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
