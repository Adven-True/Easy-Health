package com.atguigu.yygh.order.controller.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.enums.PaymentTypeEnum;
import com.atguigu.yygh.order.service.PaymentService;
import com.atguigu.yygh.order.service.WXService;
import com.atguigu.yygh.order.utils.SendMsmMq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/order/weixin")
public class WXController {

	@Autowired
	private WXService wxService;

	@Autowired
	private PaymentService paymentService;

	//generate QR code
	@GetMapping("/createNative/{orderId}")
	public Result createNative(@PathVariable Long orderId){
		Map map = wxService.createNative(orderId);
		return Result.ok(map);
	}

	@ApiOperation(value = "check payment status")
	@GetMapping("/queryPayStatus/{orderId}")
	public Result queryPayStatus(
			@ApiParam(name = "orderId", value = "order id", required = true)
			@PathVariable("orderId") Long orderId) {
		//call interface
		Map<String, String> resultMap = wxService.queryPayStatus(orderId, PaymentTypeEnum.WEIXIN.name());
		if (resultMap == null) {//error
			return Result.fail().message("pay error");
		}
		if ("SUCCESS".equals(resultMap.get("trade_state"))) {//if succeed
			//update order status
			String out_trade_no = resultMap.get("out_trade_no");
			paymentService.paySuccess(out_trade_no, PaymentTypeEnum.WEIXIN.getStatus(), resultMap);
			return Result.ok().message("pay succeed");
		}
		return Result.ok().message("payment pending");
	}

}
