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
 * RefundInfo
 * </p>
 *
 * @author xy
 */
@Data
@ApiModel(description = "RefundInfo")
@TableName("refund_info")
public class RefundInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "external business code")
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

	@ApiModelProperty(value = "refund amount")
	@TableField("total_amount")
	private BigDecimal totalAmount;

	@ApiModelProperty(value = "subject")
	@TableField("subject")
	private String subject;

	@ApiModelProperty(value = "refund status")
	@TableField("refund_status")
	private Integer refundStatus;

	@ApiModelProperty(value = "callback time")
	@TableField("callback_time")
	private Date callbackTime;

	@ApiModelProperty(value = "callback content")
	@TableField("callback_content")
	private String callbackContent;

}

