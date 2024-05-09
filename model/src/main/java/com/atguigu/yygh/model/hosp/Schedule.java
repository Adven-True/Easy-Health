package com.atguigu.yygh.model.hosp;

import com.atguigu.yygh.model.base.BaseEntity;
import com.atguigu.yygh.model.base.BaseMongoEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Schedule
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "Schedule")
@Document("Schedule")
public class Schedule extends BaseMongoEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "hospital code")
	@Indexed //normal index
	private String hoscode;

	@ApiModelProperty(value = "department code")
	@Indexed //normal index
	private String depcode;

	@ApiModelProperty(value = "professional title")
	private String title;

	@ApiModelProperty(value = "doctor name")
	private String docname;

	@ApiModelProperty(value = "Specialty Skills")
	private String skill;

	@ApiModelProperty(value = "Shift Time")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date workDate;

	@ApiModelProperty(value = "Shift Time（0：morning 1：afternoon）")
	private Integer workTime;

	@ApiModelProperty(value = "total appointment slots")
	private Integer reservedNumber;

	@ApiModelProperty(value = "available slots")
	private Integer availableNumber;

	@ApiModelProperty(value = "registration fee")
	private BigDecimal amount;

	@ApiModelProperty(value = "shift status（-1：not on service 0：not accept appointment 1：able to accept appontment）")
	private Integer status;

	@ApiModelProperty(value = "shift code(Hospital's Scheduling Primary Key)")
	@Indexed //normal index
	private String hosScheduleId;

}

