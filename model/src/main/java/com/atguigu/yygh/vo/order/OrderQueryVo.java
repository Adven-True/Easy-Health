package com.atguigu.yygh.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "Order")
public class OrderQueryVo {


	@ApiModelProperty(value = "user id")
	private Long userId;
	
	@ApiModelProperty(value = "order trade number")
	private String outTradeNo;

	@ApiModelProperty(value = "patient id")
	private Long patientId;

	@ApiModelProperty(value = "patient")
	private String patientName;

	@ApiModelProperty(value = "hospital name")
	private String keyword;

	@ApiModelProperty(value = "order status")
	private String orderStatus;

	@ApiModelProperty(value = "shift date")
	private String reserveDate;

	@ApiModelProperty(value = "create time")
	private String createTimeBegin;
	private String createTimeEnd;

}

