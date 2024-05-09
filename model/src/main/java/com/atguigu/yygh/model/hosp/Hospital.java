package com.atguigu.yygh.model.hosp;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.base.BaseEntity;
import com.atguigu.yygh.model.base.BaseMongoEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>
 * Hospital
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "Hospital")
@Document("Hospital")
public class Hospital extends BaseMongoEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "hospital code")
	@Indexed(unique = true) //unique index
	private String hoscode;

	@ApiModelProperty(value = "hospital name")
	@Indexed //normal index
	private String hosname;

	@ApiModelProperty(value = "hospital type")
	private String hostype;

	@ApiModelProperty(value = "province code")
	private String provinceCode;

	@ApiModelProperty(value = "city code")
	private String cityCode;

	@ApiModelProperty(value = "district code")
	private String districtCode;

	@ApiModelProperty(value = "detailed address")
	private String address;

	@ApiModelProperty(value = "hospital logo")
	private String logoData;

	@ApiModelProperty(value = "hospital information")
	private String intro;

	@ApiModelProperty(value = "bus route")
	private String route;

	@ApiModelProperty(value = "status 0：offline 1：online")
	private Integer status;

	//预约规则
	@ApiModelProperty(value = "booking rule")
	private BookingRule bookingRule;

	public void setBookingRule(String bookingRule) {
		this.bookingRule = JSONObject.parseObject(bookingRule, BookingRule.class);
	}

}

