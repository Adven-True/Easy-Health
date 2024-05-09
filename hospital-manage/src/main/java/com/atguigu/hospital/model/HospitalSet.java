package com.atguigu.hospital.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * HospitalSet
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "HospitalSet")
@TableName("hospital_set")
public class HospitalSet extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Hospital ID")
	private String hoscode;

	@ApiModelProperty(value = "Signature Key")
	private String signKey;

	@ApiModelProperty(value = "API Base Path")
	private String apiUrl;

}

