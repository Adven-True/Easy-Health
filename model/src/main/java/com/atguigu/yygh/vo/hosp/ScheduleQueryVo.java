package com.atguigu.yygh.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "Schedule")
public class ScheduleQueryVo {
	
	@ApiModelProperty(value = "hospital code")
	private String hoscode;

	@ApiModelProperty(value = "department code")
	private String depcode;

	@ApiModelProperty(value = "doctor code")
	private String doccode;

	@ApiModelProperty(value = "shift date")
	private Date workDate;

	@ApiModelProperty(value = "shift time（0：morning 1：afternoon）")
	private Integer workTime;

}

