package com.atguigu.yygh.order.utils;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.RefundInfo;
import com.atguigu.yygh.rabbit.constant.MqConst;
import com.atguigu.yygh.rabbit.service.RabbitService;
import com.atguigu.yygh.vo.msm.MsmVo;
import com.atguigu.yygh.vo.order.OrderMqVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class SendMsmMq {

	private SendMsmMq() {
	}


	public static void sendMq(OrderInfo orderInfo,String TemplateCode, RabbitService rabbitService) {

		OrderMqVo orderMqVo = new OrderMqVo();
		orderMqVo.setScheduleId(orderInfo.getScheduleId());
		//message
		MsmVo msmVo = new MsmVo();
		msmVo.setMail(orderInfo.getPatientMail());
		msmVo.setTemplateCode(TemplateCode);
		String reserveDate = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + (orderInfo.getReserveTime()==0 ? "am": "pm");
		Map<String,Object> param = new HashMap<String,Object>(){{
			put("title", orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle());
			put("reserveDate", reserveDate);
			put("hosname",orderInfo.getHosname());
			put("name", orderInfo.getPatientName());
			put("fetchTime",orderInfo.getFetchTime());
			put("quitTime",orderInfo.getQuitTime());
		}};
		msmVo.setParam(param);
		orderMqVo.setMsmVo(msmVo);
		rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);
	}

	//refund succeed
	public static void sendMq(OrderInfo orderInfo,RefundInfo refundInfo, String TemplateCode, RabbitService rabbitService) {
		 OrderMqVo orderMqVo = new OrderMqVo();
		orderMqVo.setScheduleId(orderInfo.getScheduleId());
		//message
		MsmVo msmVo = new MsmVo();
		msmVo.setMail(orderInfo.getPatientMail());
		msmVo.setTemplateCode(TemplateCode);
		String reserveDate = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + (orderInfo.getReserveTime()==0 ? "am": "pm");
		Map<String,Object> param = new HashMap<String,Object>(){{
			put("title", orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle());
			put("hosname",orderInfo.getHosname());
			put("reserveDate", reserveDate);
			put("name", orderInfo.getPatientName());
			put("totalAmount",refundInfo.getTotalAmount());
		}};
		msmVo.setParam(param);
		orderMqVo.setMsmVo(msmVo);
		rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);
	}


	public static void sendMq(String scheduleId, OrderInfo orderInfo, Integer reservedNumber, Integer availableNumber, RabbitService rabbitService) {

		OrderMqVo orderMqVo = new OrderMqVo();
		orderMqVo.setScheduleId(scheduleId);
		orderMqVo.setReservedNumber(reservedNumber);
		orderMqVo.setAvailableNumber(availableNumber);


		MsmVo msmVo = new MsmVo();
		msmVo.setMail(orderInfo.getPatientMail());
		msmVo.setTemplateCode("order");
		String reserveDate =
				new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")
						+ (orderInfo.getReserveTime()==0 ? "am": "pm");
		Map<String,Object> param = new HashMap<String,Object>(){{

			put("title", orderInfo.getHosname()+"|"+orderInfo.getDepname());
			put("hosname",orderInfo.getHosname());

			put("amount", orderInfo.getAmount());

			put("reserveDate", reserveDate);

			put("name", orderInfo.getPatientName());

			put("quitTime", new DateTime(orderInfo.getQuitTime()).toString("yyyy-MM-dd HH:mm"));
		}};
		msmVo.setParam(param);

		orderMqVo.setMsmVo(msmVo);
		rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);
	}


}
