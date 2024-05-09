package com.atguigu.yygh.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "OrderCountQueryVo")
public class OrderCountQueryVo {
	
	@ApiModelProperty(value = "hospital code")
	private String hoscode;

	@ApiModelProperty(value = "hospital name")
	private String hosname;

	@ApiModelProperty(value = "shift date")
	private String reserveDateBegin;
	private String reserveDateEnd;

}

