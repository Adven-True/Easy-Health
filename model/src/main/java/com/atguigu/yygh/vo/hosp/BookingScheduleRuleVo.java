package com.atguigu.yygh.vo.hosp;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(description = "Appointment Scheduling Rules Data")
public class BookingScheduleRuleVo {
	
	@ApiModelProperty(value = "available date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date workDate;

	@ApiModelProperty(value = "available date")
	@JsonFormat(pattern = "MM月dd日")
	private Date workDateMd;

	@ApiModelProperty(value = "day of week")
	private String dayOfWeek;

	@ApiModelProperty(value = "doctor number")
	private Integer docCount;

	@ApiModelProperty(value = "total available department number")
	private Integer reservedNumber;

	@ApiModelProperty(value = "available appointment slots")
	private Integer availableNumber;

	@ApiModelProperty(value = "Status 0: Normal, 1: Upcoming Appointment Availability, -1: Registration Closed Today")
	private Integer status;
}

