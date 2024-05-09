package com.atguigu.yygh.model.order;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * PaymentInfo
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "PaymentInfo")
@TableName("payment_info")
public class PaymentInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "external business number")
	@TableField("out_trade_no")
	private String outTradeNo;

	@ApiModelProperty(value = "order number")
	@TableField("order_id")
	private Long orderId;

	@ApiModelProperty(value = "payment type(alipay, wechat)")
	@TableField("payment_type")
	private Integer paymentType;

	@ApiModelProperty(value = "trade number")
	@TableField("trade_no")
	private String tradeNo;

	@ApiModelProperty(value = "payment amount")
	@TableField("total_amount")
	private BigDecimal totalAmount;

	@ApiModelProperty(value = "subject")
	@TableField("subject")
	private String subject;

	@ApiModelProperty(value = "payment status")
	@TableField("payment_status")
	private Integer paymentStatus;

	@ApiModelProperty(value = "callback time")
	@TableField("callback_time")
	private Date callbackTime;

	@ApiModelProperty(value = "callback content")
	@TableField("callback_content")
	private String callbackContent;

}

