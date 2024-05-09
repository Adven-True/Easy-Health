package com.atguigu.yygh.rabbit.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	/**
	 *  send message
	 * @param exchange
	 * @param routingKey
	 * @param message
	 */
	public boolean sendMessage(String exchange, String routingKey, Object message) {
		rabbitTemplate.convertAndSend(exchange, routingKey, message);
		return true;
	}
}
