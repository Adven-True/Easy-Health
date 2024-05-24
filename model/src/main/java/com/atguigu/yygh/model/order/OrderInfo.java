package com.atguigu.yygh.model.order;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@ApiModel(description = "Order")
@TableName("order_info")
public class OrderInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "userId")
	@TableField("user_id")
	private Long userId;

	@ApiModelProperty(value = "order trade number")
	@TableField("out_trade_no")
	private String outTradeNo;

	@ApiModelProperty(value = "hospital code")
	@TableField("hoscode")
	private String hoscode;

	@ApiModelProperty(value = "hospital name")
	@TableField("hosname")
	private String hosname;

	@ApiModelProperty(value = "department number")
	@TableField("depcode")
	private String depcode;

	@ApiModelProperty(value = "department name")
	@TableField("depname")
	private String depname;

	@ApiModelProperty(value = "shift time id")
	@TableField("schedule_id")
	private String scheduleId;

	@ApiModelProperty(value = "professional title")
	@TableField("title")
	private String title;

	@ApiModelProperty(value = "shift time")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@TableField("reserve_date")
	private Date reserveDate;

	@ApiModelProperty(value = "appointment time（0：morning 1：afternoon）")
	@TableField("reserve_time")
	private Integer reserveTime;

	@ApiModelProperty(value = "patient id")
	@TableField("patient_id")
	private Long patientId;

	@ApiModelProperty(value = "patient name")
	@TableField("patient_name")
	private String patientName;

	@ApiModelProperty(value = "patient email address")
	@TableField("patient_mail")
	private String patientMail;

	@ApiModelProperty(value = "Unique Identifier for Appointment Records (Primary Key for Hospital Appointment Records)")
	@TableField("hos_record_id")
	private String hosRecordId;

	@ApiModelProperty(value = "appointment number sequence")
	@TableField("number")
	private Integer number;

	@ApiModelProperty(value = "check in time")
	@TableField("fetch_time")
	private String fetchTime;

	@ApiModelProperty(value = "check in address")
	@TableField("fetch_address")
	private String fetchAddress;

	@ApiModelProperty(value = "service fee")
	@TableField("amount")
	private BigDecimal amount;

	@ApiModelProperty(value = "quit time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@TableField("quit_time")
	private Date quitTime;

	@ApiModelProperty(value = "order status")
	@TableField("order_status")
	private Integer orderStatus;

}

