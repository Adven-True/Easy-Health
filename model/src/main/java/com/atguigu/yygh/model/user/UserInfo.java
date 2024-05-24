package com.atguigu.yygh.model.user;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "UserInfo")
@TableName("user_info")
public class UserInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "wechat openid")
	@TableField("openid")
	private String openid;

	@ApiModelProperty(value = "nick name")
	@TableField("nick_name")
	private String nickName;

	@ApiModelProperty(value = "mail address")
	@TableField("mail")
	private String mail;

	@ApiModelProperty(value = "phone number")
	@TableField("phone")
	private String phone;

	@ApiModelProperty(value = "name")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "ID type")
	@TableField("certificates_type")
	private String certificatesType;

	@ApiModelProperty(value = "ID number")
	@TableField("certificates_no")
	private String certificatesNo;

	@ApiModelProperty(value = "ID path")
	@TableField("certificates_url")
	private String certificatesUrl;

	@ApiModelProperty(value = "Authentication Status (0: Not Verified, 1: Verifying, 2: Verified, -1: Verification Failed)")
	@TableField("auth_status")
	private Integer authStatus;

	@ApiModelProperty(value = "status（0：locked 1：normal）")
	@TableField("status")
	private Integer status;

}

