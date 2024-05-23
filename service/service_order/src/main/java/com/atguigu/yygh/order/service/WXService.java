package com.atguigu.yygh.order.service;

import java.util.Map;

public interface WXService {

	Map createNative(Long orderId);


	Map queryPayStatus(Long orderId, String paymentType);


	Boolean refund(Long orderId);
}
