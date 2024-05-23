package com.atguigu.hospital.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


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
	private String birthdate;

	@ApiModelProperty(value = "phone number")
	@TableField("phone")
	private String phone;

	@ApiModelProperty(value = "married or not")
	@TableField("is_marry")
	private Integer isMarry;

	@ApiModelProperty(value = "province code")
	@TableField("province_code")
	private String provinceCode;

	@ApiModelProperty(value = "city code")
	@TableField("city_code")
	private String cityCode;

	@ApiModelProperty(value = "district code")
	@TableField("district_code")
	private String districtCode;

	@ApiModelProperty(value = "Details Address")
	@TableField("address")
	private String address;

	@ApiModelProperty(value = "Emergency Contact Person's Name")
	@TableField("contacts_name")
	private String contactsName;

	@ApiModelProperty(value = "Emergency Contact Person's Identification Type")
	@TableField("contacts_certificates_type")
	private String contactsCertificatesType;

	@ApiModelProperty(value = "Emergency Contact Person's ID number")
	@TableField("contacts_certificates_no")
	private String contactsCertificatesNo;

	@ApiModelProperty(value = "Emergency Contact Person's phone number")
	@TableField("contacts_phone")
	private String contactsPhone;

}

