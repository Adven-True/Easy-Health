package com.atguigu.yygh.vo.msm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@ApiModel(description = "message entity")
public class MsmVo {

    @ApiModelProperty(value = "mail")
    private String mail;

    @ApiModelProperty(value = "message template code")
    private String templateCode;

    @ApiModelProperty(value = "message template params")
    private Map<String,Object> param;
}
