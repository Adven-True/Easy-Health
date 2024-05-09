package com.atguigu.yygh.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="user search object")
public class UserInfoQueryVo {

    @ApiModelProperty(value = "key word")
    private String keyword;

    @ApiModelProperty(value = "status")
    private Integer status;

    @ApiModelProperty(value = "authentication status")
    private Integer authStatus;

    @ApiModelProperty(value = "ceate time")
    private String createTimeBegin;

    @ApiModelProperty(value = "create time")
    private String createTimeEnd;

}
