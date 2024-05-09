package com.atguigu.hospital.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Schedule
 * </p>
 *
 * @author qy
 */
@Data
@ApiModel(description = "Schedule")
@TableName("schedule")
public class Schedule extends BaseNoAutoEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "hospital if")
	@TableField("hoscode")
	private String hoscode;

	@ApiModelProperty(value = "department id")
	@TableField("depcode")
	private String depcode;

	@ApiModelProperty(value = "professional title")
	@TableField("title")
	private String title;

	@ApiModelProperty(value = "doctor's name")
	@TableField("docname")
	private String docname;

	@ApiModelProperty(value = "Specialty Skills")
	@TableField("skill")
	private String skill;

	@ApiModelProperty(value = "Scheduling Date")
	@TableField("work_date")
	private String workDate;

	@ApiModelProperty(value = "Scheduling Time（0：morning 1：afternoon）")
	@TableField("work_time")
	private Integer workTime;

	@ApiModelProperty(value = "Available Appointment Slots")
	@TableField("reserved_number")
	private Integer reservedNumber;

	@ApiModelProperty(value = "Remaining Appointment Slots")
	@TableField("available_number")
	private Integer availableNumber;

	@ApiModelProperty(value = "Registration Fee")
	@TableField("amount")
	private String amount;

	@ApiModelProperty(value = "Scheduling Status（-1：not on service 0：no more appointment slots 1：available）")
	@TableField("status")
	private Integer status;
}

