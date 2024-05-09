package com.atguigu.yygh.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="login user")
public class LoginVo {

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "email id")
    private String mail;

    @ApiModelProperty(value = "phone number")
    private String phone;

    @ApiModelProperty(value = "password")
    private String code;

    @ApiModelProperty(value = "IP")
    private String ip;
}
