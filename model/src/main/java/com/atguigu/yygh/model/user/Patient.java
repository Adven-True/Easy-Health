package com.atguigu.yygh.model.user;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * Patient
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "Patient")
@TableName("patient")
public class Patient extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "user id")
	@TableField("user_id")
	private Long userId;

	@ApiModelProperty(value = "name")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "ID type")
	@TableField("certificates_type")
	private String certificatesType;

	@ApiModelProperty(value = "ID number")
	@TableField("certificates_no")
	private String certificatesNo;

	@ApiModelProperty(value = "gender")
	@TableField("sex")
	private Integer sex;

	@ApiModelProperty(value = "birth date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@TableField("birthdate")
	private Date birthdate;

	@ApiModelProperty(value = "email address")
	@TableField("mail")
	private String mail;

	@ApiModelProperty(value = "married or not")
	@TableField("is_marry")
	private Integer isMarry;

	@ApiModelProperty(value = "=province code")
	@TableField("province_code")
	private String provinceCode;

	@ApiModelProperty(value = "city code")
	@TableField("city_code")
	private String cityCode;

	@ApiModelProperty(value = "district code")
	@TableField("district_code")
	private String districtCode;

	@ApiModelProperty(value = "detailed address")
	@TableField("address")
	private String address;

	@ApiModelProperty(value = "contact name")
	@TableField("contacts_name")
	private String contactsName;

	@ApiModelProperty(value = "contacts certificates type")
	@TableField("contacts_certificates_type")
	private String contactsCertificatesType;

	@ApiModelProperty(value = "contacts certificates no")
	@TableField("contacts_certificates_no")
	private String contactsCertificatesNo;

	@ApiModelProperty(value = "contacts phone")
	@TableField("contacts_phone")
	private String contactsPhone;

	@ApiModelProperty(value = "is insured or not")
	@TableField("is_insure")
	private Integer isInsure;

	@ApiModelProperty(value = "insurance card number")
	@TableField("card_no")
	private String cardNo;

	@ApiModelProperty(value = "status（0：default 1：verified）")
	@TableField("status")
	private String status;
}

