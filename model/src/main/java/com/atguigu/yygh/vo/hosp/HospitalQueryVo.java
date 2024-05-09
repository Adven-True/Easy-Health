package com.atguigu.yygh.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "Hospital")
public class HospitalQueryVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "hospital code")
	private String hoscode;

	@ApiModelProperty(value = "hospital name")
	private String hosname;

	@ApiModelProperty(value = "hospital type")
	private String hostype;

	@ApiModelProperty(value = "province code")
	private String provinceCode;

	@ApiModelProperty(value = "city code")
	private String cityCode;

	@ApiModelProperty(value = "district code")
	private String districtCode;

	@ApiModelProperty(value = "status")
	private Integer status;
}

