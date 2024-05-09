package com.atguigu.yygh.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "OrderCountVo")
public class OrderCountVo {
	
	@ApiModelProperty(value = "shift date")
	private String reserveDate;

	@ApiModelProperty(value = "order count")
	private Integer count;

}

