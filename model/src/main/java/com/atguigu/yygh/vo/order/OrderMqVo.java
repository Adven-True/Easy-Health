package com.atguigu.yygh.vo.order;

import com.atguigu.yygh.vo.msm.MsmVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "OrderMqVo")
public class OrderMqVo {

	@ApiModelProperty(value = "total available slots")
	private Integer reservedNumber;

	@ApiModelProperty(value = "available slots")
	private Integer availableNumber;

	@ApiModelProperty(value = "shift id")
	private String scheduleId;

	@ApiModelProperty(value = "message entity")
	private MsmVo msmVo;

}

