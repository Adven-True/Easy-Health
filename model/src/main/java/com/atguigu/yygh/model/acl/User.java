package com.atguigu.yygh.model.acl;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "user")
@TableName("acl_user")
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "user name")
	@TableField("username")
	private String username;

	@ApiModelProperty(value = "password")
	@TableField("password")
	private String password;

	@ApiModelProperty(value = "nick name")
	@TableField("nick_name")
	private String nickName;

	@ApiModelProperty(value = "user avatar")
	@TableField("salt")
	private String salt;

	@ApiModelProperty(value = "user token")
	@TableField("token")
	private String token;

}



