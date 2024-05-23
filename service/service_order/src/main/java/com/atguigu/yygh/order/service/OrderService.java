package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {

    Long saveOrder(String scheduleId, Long patientId);

    Page<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);


    Page<OrderInfo> selectPageByUserId(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    OrderInfo getOrderInfo(Long id);


    Map<String,Object> show(Long orderId);


    Boolean cancelOrder(Long orderId);


    void patientTips();


    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}