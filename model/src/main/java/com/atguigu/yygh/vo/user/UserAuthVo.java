package com.atguigu.yygh.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="member authentication object")
public class UserAuthVo {

    @ApiModelProperty(value = "user name")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "ID type")
    @TableField("certificates_type")
    private String certificatesType;

    @ApiModelProperty(value = "ID number")
    @TableField("certificates_no")
    private String certificatesNo;

    @ApiModelProperty(value = "ID url")
    @TableField("certificates_url")
    private String certificatesUrl;

}
