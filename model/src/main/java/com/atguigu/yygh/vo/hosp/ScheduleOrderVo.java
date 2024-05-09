package com.atguigu.yygh.vo.hosp;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "Schedule")
public class ScheduleOrderVo {

	@ApiModelProperty(value = "hospital code")
	private String hoscode;

	@ApiModelProperty(value = "hospital name")
	private String hosname;

	@ApiModelProperty(value = "department code")
	private String depcode;

	@ApiModelProperty(value = "department name")
	private String depname;

	@ApiModelProperty(value = "shift code")
	private String hosScheduleId;

	@ApiModelProperty(value = "professional type")
	private String title;

	@ApiModelProperty(value = "shift date")
	private Date reserveDate;

	@ApiModelProperty(value = "available appointment slots")
	private Integer availableNumber;

	@ApiModelProperty(value = "shift time（0：morning 1：afternoon）")
	private Integer reserveTime;

	@ApiModelProperty(value = "service fee")
	private BigDecimal amount;

	@ApiModelProperty(value = "quit time")
	private Date quitTime;

	@ApiModelProperty(value = "registration start time")
	private Date startTime;

	@ApiModelProperty(value = "registration end time")
	private Date endTime;

	@ApiModelProperty(value = "registration end time of the day")
	private Date stopTime;
}

