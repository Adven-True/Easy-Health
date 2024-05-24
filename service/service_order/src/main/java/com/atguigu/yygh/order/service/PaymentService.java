package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {

	void savePaymentInfo(OrderInfo order, Integer status);


	void paySuccess(String outTradeNo, Integer paymentType, Map<String, String> paramMap);


	PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);
}
