package com.atguigu.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.enums.PaymentTypeEnum;
import com.atguigu.yygh.enums.RefundStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.model.order.RefundInfo;
import com.atguigu.yygh.order.service.OrderService;
import com.atguigu.yygh.order.service.PaymentService;
import com.atguigu.yygh.order.service.RefundInfoService;
import com.atguigu.yygh.order.service.WXService;
import com.atguigu.yygh.order.utils.ConstantPropertiesUtils;
import com.atguigu.yygh.order.utils.HttpClient;
import com.atguigu.yygh.order.utils.SendMsmMq;
import com.atguigu.yygh.rabbit.service.RabbitService;
import com.atguigu.yygh.vo.msm.MsmVo;
import com.atguigu.yygh.vo.order.OrderMqVo;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WXServiceImpl implements WXService {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private RefundInfoService refundInfoService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitService rabbitService;
	//generate QR code
	@Override
	public Map createNative(Long orderId) {
		try {

			Map payMap = (Map) redisTemplate.opsForValue().get(orderId.toString());
			if (payMap!=null){
				return payMap;
			}

			OrderInfo order = orderService.getById(orderId);


			Map paramMap = new HashMap();
			paramMap.put("appid", ConstantPropertiesUtils.APPID);
			paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
			paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
			String body = order.getReserveDate() + "check in"+ order.getDepname();
			paramMap.put("body", body);
			paramMap.put("out_trade_no", order.getOutTradeNo());
			//paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
			paramMap.put("total_fee", "1");//
			paramMap.put("spbill_create_ip", "127.0.0.1");
			paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
			paramMap.put("trade_type", "NATIVE");
			//generate interface
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			//set params
			client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY));
			client.setHttps(true);
			client.post();
			//return data
			String xml = client.getContent();
			//xml to map
			Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
			System.out.println("resultMap:"+resultMap);
			//encapsulation
			Map map = new HashMap<>();
			if (resultMap!=null){
				//add records
				paymentService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());
				map.put("orderId", orderId);
				map.put("totalFee", order.getAmount());
				map.put("resultCode", resultMap.get("result_code"));
				map.put("codeUrl", resultMap.get("code_url"));
				//redis
				if (resultMap.get("result_code")!=null){
					redisTemplate.opsForValue().set(orderId.toString(),map,120, TimeUnit.MINUTES);
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map queryPayStatus(Long orderId, String paymentType) {
		try {
			OrderInfo orderInfo = orderService.getById(orderId);
			//1、encapsulation
			Map paramMap = new HashMap<>();
			paramMap.put("appid", ConstantPropertiesUtils.APPID);
			paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
			paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
			paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
			//2、request
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
			client.setHttps(true);
			client.post();
			//3、return data to Map
			String xml = client.getContent();
			Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
			//4、return
			return resultMap;
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public Boolean refund(Long orderId) {
		try {
			OrderInfo orderInfo = orderService.getById(orderId);
			PaymentInfo paymentInfoQuery = paymentService.getPaymentInfo(orderId, PaymentTypeEnum.WEIXIN.getStatus());

			RefundInfo refundInfo = refundInfoService.saveRefundInfo(paymentInfoQuery);
			if(refundInfo.getRefundStatus().intValue() == RefundStatusEnum.REFUND.getStatus().intValue()) {
				return true;
			}
			Map<String,String> paramMap = new HashMap<>(8);
			paramMap.put("appid",ConstantPropertiesUtils.APPID);       //Public account ID
			paramMap.put("mch_id",ConstantPropertiesUtils.PARTNER);   //Merchant ID
			paramMap.put("nonce_str",WXPayUtil.generateNonceStr());
			paramMap.put("transaction_id",paymentInfoQuery.getTradeNo()); //WeChat order number
			paramMap.put("out_trade_no",paymentInfoQuery.getOutTradeNo()); //Merchant order number
			paramMap.put("out_refund_no","tk"+paymentInfoQuery.getOutTradeNo()); //Merchant refund number
//       paramMap.put("total_fee",paymentInfoQuery.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
//       paramMap.put("refund_fee",paymentInfoQuery.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
			paramMap.put("total_fee","1");
			paramMap.put("refund_fee","1");
			String paramXml = WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY);
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/secapi/pay/refund");
			client.setXmlParam(paramXml);
			client.setHttps(true);
			client.setCert(true);
			client.setCertPassword(ConstantPropertiesUtils.PARTNER);
			client.post();
			//3、return third party data
			String xml = client.getContent();
			Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
			if (WXPayConstants.SUCCESS.equalsIgnoreCase(resultMap.get("result_code"))) {
				refundInfo.setCallbackTime(new Date());
				refundInfo.setTradeNo(resultMap.get("refund_id"));
				refundInfo.setRefundStatus(RefundStatusEnum.REFUND.getStatus());
				refundInfo.setCallbackContent(JSONObject.toJSONString(resultMap));
				refundInfoService.updateById(refundInfo);

				SendMsmMq.sendMq(orderInfo,refundInfo,"refund succeed",rabbitService);
				return true;
			}
			return false;
		}  catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
