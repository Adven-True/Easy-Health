package com.atguigu.yygh.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="registration object")
public class RegisterVo {

    @ApiModelProperty(value = "email")
    private String mail;

    @ApiModelProperty(value = "password")
    private String password;

    @ApiModelProperty(value = "verification code")
    private String code;
}
