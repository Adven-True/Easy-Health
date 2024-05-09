package com.atguigu.hospital.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * OrderInfo
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "OrderInfo")
@TableName("order_info")
public class OrderInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "Scheduling ID")
	@TableField("schedule_id")
	private Long scheduleId;

	@ApiModelProperty(value = "Patient ID")
	@TableField("patient_id")
	private Long patientId;

	@ApiModelProperty(value = "Appointment Number Sequence")
	@TableField("number")
	private Integer number;

	@ApiModelProperty(value = "Recommended Appointment Time")
	@TableField("fetch_time")
	private String fetchTime;

	@ApiModelProperty(value = "Location for Ticket Collection")
	@TableField("fetch_address")
	private String fetchAddress;

	@ApiModelProperty(value = "Medical Service Fee")
	@TableField("amount")
	private BigDecimal amount;

	@ApiModelProperty(value = "Payment Time")
	@TableField("pay_time")
	private Date payTime;

	@ApiModelProperty(value = "Quit Time")
	@TableField("quit_time")
	private Date quitTime;

	@ApiModelProperty(value = "Order Status")
	@TableField("order_status")
	private Integer orderStatus;

}

