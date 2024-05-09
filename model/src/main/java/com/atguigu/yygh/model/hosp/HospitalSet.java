package com.atguigu.yygh.model.hosp;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * HospitalSet
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "hospital set")
@TableName("hospital_set")
public class HospitalSet extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "hospital name")
	@TableField("hosname")
	private String hosname;

	@ApiModelProperty(value = "hospital code")
	@TableField("hoscode")
	private String hoscode;

	@ApiModelProperty(value = "API Base Path")
	@TableField("api_url")
	private String apiUrl;

	@ApiModelProperty(value = "Signature Key")
	@TableField("sign_key")
	private String signKey;

	@ApiModelProperty(value = "emergency contact name")
	@TableField("contacts_name")
	private String contactsName;

	@ApiModelProperty(value = "contact email address")
	@TableField("contacts_mail")
	private String contactsMail;

	@ApiModelProperty(value = "status")
	@TableField("status")
	private Integer status;

}

