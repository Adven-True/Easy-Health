package com.atguigu.yygh.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Department")
public class DepartmentQueryVo {
	
	@ApiModelProperty(value = "hospital code")
	private String hoscode;

	@ApiModelProperty(value = "department code")
	private String depcode;

	@ApiModelProperty(value = "department name")
	private String depname;

	@ApiModelProperty(value = "big department code")
	private String bigcode;

	@ApiModelProperty(value = "big department name")
	private String bigname;

}

