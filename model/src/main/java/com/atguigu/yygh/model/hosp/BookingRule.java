package com.atguigu.yygh.model.hosp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.base.BaseEntity;
import com.atguigu.yygh.model.base.BaseMongoEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * RegisterRule
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "booking rule")
@Document("BookingRule")
public class BookingRule {
	
	@ApiModelProperty(value = "appointment circle")
	private Integer cycle;

	@ApiModelProperty(value = "appointment slot release time")
	private String releaseTime;

	@ApiModelProperty(value = "stop time")
	private String stopTime;

	@ApiModelProperty(value = "time before ddl（eg：before the appointment day -1，on appointment day0）")
	private Integer quitDay;

	@ApiModelProperty(value = "quit time")
	private String quitTime;

	@ApiModelProperty(value = "booking rule")
	private List<String> rule;

	/**
	 *
	 * @param rule
	 */
	public void setRule(String rule) {
		if(!StringUtils.isEmpty(rule)) {
			this.rule = JSONArray.parseArray(rule, String.class);
		}
	}

}

