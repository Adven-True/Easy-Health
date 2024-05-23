package com.atguigu.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.hosp.client.HospitalFeignClient;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.order.mapper.OrderMapper;
import com.atguigu.yygh.order.service.OrderService;
import com.atguigu.yygh.order.service.WXService;
import com.atguigu.yygh.order.utils.SendMsmMq;
import com.atguigu.yygh.rabbit.constant.MqConst;
import com.atguigu.yygh.rabbit.service.RabbitService;
import com.atguigu.yygh.user.client.PatientFeignClient;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.atguigu.yygh.vo.msm.MsmVo;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import com.atguigu.yygh.vo.order.OrderCountVo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.atguigu.yygh.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl extends
        ServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private PatientFeignClient patientFeignClient;
    @Autowired
    private HospitalFeignClient hospitalFeignClient;

    @Autowired
    private WXService wxService;

    //save order
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        Patient patient = patientFeignClient.getPatient(patientId);
        if(null == patient) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        ScheduleOrderVo scheduleOrderVo = hospitalFeignClient.getScheduleOrderVo(scheduleId);
        if(null == scheduleOrderVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        if(new DateTime(scheduleOrderVo.getStartTime()).isAfterNow()
                || new DateTime(scheduleOrderVo.getEndTime()).isBeforeNow()) {
            throw new YyghException(ResultCodeEnum.TIME_NO);
        }
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(scheduleOrderVo.getHoscode());
        if(null == scheduleOrderVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        if(scheduleOrderVo.getAvailableNumber() <= 0) {
            throw new YyghException(ResultCodeEnum.NUMBER_NO);
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(scheduleOrderVo, orderInfo);
        String outTradeNo = System.currentTimeMillis() + ""+ new Random().nextInt(100);
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setScheduleId(scheduleOrderVo.getHosScheduleId());
        orderInfo.setUserId(patient.getUserId());
        orderInfo.setPatientId(patientId);
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientMail(patient.getMail());
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());
        this.save(orderInfo);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",orderInfo.getHoscode());
        paramMap.put("depcode",orderInfo.getDepcode());
        paramMap.put("hosScheduleId",orderInfo.getScheduleId());
        paramMap.put("reserveDate",new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd"));
        paramMap.put("reserveTime", orderInfo.getReserveTime());
        paramMap.put("amount",orderInfo.getAmount());
        paramMap.put("name", patient.getName());
        paramMap.put("certificatesType",patient.getCertificatesType());
        paramMap.put("certificatesNo", patient.getCertificatesNo());
        paramMap.put("sex",patient.getSex());
        paramMap.put("birthdate", patient.getBirthdate());
        paramMap.put("mail",patient.getMail());
        paramMap.put("isMarry", patient.getIsMarry());
        paramMap.put("provinceCode",patient.getProvinceCode());
        paramMap.put("cityCode", patient.getCityCode());
        paramMap.put("districtCode",patient.getDistrictCode());
        paramMap.put("address",patient.getAddress());
        //contact person
        paramMap.put("contactsName",patient.getContactsName());
        paramMap.put("contactsCertificatesType", patient.getContactsCertificatesType());
        paramMap.put("contactsCertificatesNo",patient.getContactsCertificatesNo());
        paramMap.put("contactsPhone",patient.getContactsPhone());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
//        String sign = HttpRequestHelper.getSign(paramMap, signInfoVo.getSignKey());
        paramMap.put("sign", signInfoVo.getSignKey());
        JSONObject result = HttpRequestHelper.sendRequest(paramMap, signInfoVo.getApiUrl()+"/order/submitOrder");

        if(result.getInteger("code") == 200) {
            JSONObject jsonObject = result.getJSONObject("data");
            //main key
            String hosRecordId = jsonObject.getString("hosRecordId");
            //number
            Integer number = jsonObject.getInteger("number");
            //time
            String fetchTime = jsonObject.getString("fetchTime");
            //address
            String fetchAddress = jsonObject.getString("fetchAddress");
            //refund time
            String quitTime = jsonObject.getString("quitTime");
            //update order
            orderInfo.setHosRecordId(hosRecordId);
            orderInfo.setNumber(number);
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
//            orderInfo.setQuitTime(quitTime);
            baseMapper.updateById(orderInfo);
            //total slots
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            //available slots
            Integer availableNumber = jsonObject.getInteger("availableNumber");
            SendMsmMq.sendMq(scheduleId, orderInfo, reservedNumber, availableNumber,rabbitService);
        } else {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
        return orderInfo.getId();
    }


    //order list
    @Override
    public Page<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo) {
        //orderQueryVo
        String name = orderQueryVo.getKeyword(); //医院名称
        String patientName = orderQueryVo.getPatientName(); //就诊人id
        String outTradeNo = orderQueryVo.getOutTradeNo();
        String orderStatus = orderQueryVo.getOrderStatus(); //订单状态
        String reserveDate = orderQueryVo.getReserveDate();//安排时间
        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderQueryVo.getCreateTimeEnd();
        //null cases
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)) {
            wrapper.like("hosname",name);
        }
        if(StringUtils.isNotBlank(outTradeNo)) {
            wrapper.eq("out_trade_no",outTradeNo);
        }
        if(StringUtils.isNotBlank(patientName)) {
            wrapper.like("patient_name",patientName);
        }
        if(!StringUtils.isEmpty(orderStatus)) {
            wrapper.eq("order_status",orderStatus);
        }
        if(!StringUtils.isEmpty(reserveDate)) {
            wrapper.ge("reserve_date",reserveDate);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }
        Page<OrderInfo> pages = null;
        try {

            pages = baseMapper.selectPage(pageParam, wrapper);

            if (pages!=null) {
                pages.getRecords().forEach(this::packOrderInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pages;
    }


    @Override
    public Page<OrderInfo> selectPageByUserId(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo) {
        //orderQueryVo获取条件值
        Long userId = orderQueryVo.getUserId();
        String name = orderQueryVo.getKeyword(); //hosp name
        Long patientId = orderQueryVo.getPatientId(); //patient id
        String orderStatus = orderQueryVo.getOrderStatus(); //order status
        String reserveDate = orderQueryVo.getReserveDate();//reservation time
        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderQueryVo.getCreateTimeEnd();
        //null cases
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)) {
            wrapper.like("hosname",name);
        }
        if(userId != null) {
            wrapper.eq("user_id",userId);
        }
        if(patientId != null) {
            wrapper.eq("patient_id",patientId);
        }
        if(!StringUtils.isEmpty(orderStatus)) {
            wrapper.eq("order_status",orderStatus);
        }
        if(!StringUtils.isEmpty(reserveDate)) {
            wrapper.ge("reserve_date",reserveDate);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }

        Page<OrderInfo> pages = baseMapper.selectPage(pageParam, wrapper);

        if (pages!=null) {
            pages.getRecords().forEach(this::packOrderInfo);
        }
        return pages;
    }


    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        return this.packOrderInfo(orderInfo);
    }

    @Override
    public Map<String, Object> show(Long orderId) {
        Map<String, Object> map = new HashMap<>();
        OrderInfo orderInfo = this.packOrderInfo(this.getById(orderId));
        map.put("orderInfo", orderInfo);
        Patient patient
                =  patientFeignClient.getPatient(orderInfo.getPatientId());
        map.put("patient", patient);
        return map;
    }

    @Override
    public Boolean cancelOrder(Long orderId) {
        OrderInfo orderInfo = this.getById(orderId);

        DateTime quitTime = new DateTime(orderInfo.getQuitTime());
        if(quitTime.isBeforeNow()) {
            throw new YyghException(ResultCodeEnum.CANCEL_ORDER_NO);
        }
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode());
        if(null == signInfoVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode",orderInfo.getHoscode());
        reqMap.put("hosRecordId",orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        reqMap.put("sign", signInfoVo.getSignKey());

        JSONObject result = HttpRequestHelper.sendRequest(reqMap, signInfoVo.getApiUrl()+"/order/updateCancelStatus");

        if(result.getInteger("code") != 200) {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        } else {

            if(orderInfo.getOrderStatus().intValue() == OrderStatusEnum.PAID.getStatus().intValue()) {

                boolean isRefund = wxService.refund(orderId);
                if(!isRefund) {
                    throw new YyghException(ResultCodeEnum.CANCEL_ORDER_FAIL);
                }
            }

            orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
            this.updateById(orderInfo);
            SendMsmMq.sendMq(orderInfo,"cancel order",rabbitService);
        }
        return true;
    }

    @Override
    public void patientTips() {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reserve_date",new DateTime().toString("yyyy-MM-dd"));
        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        for(OrderInfo orderInfo : orderInfoList) {

            MsmVo msmVo = new MsmVo();
            msmVo.setMail(orderInfo.getPatientMail());
            msmVo.setTemplateCode("check reminder");
            String reserveDate = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + (orderInfo.getReserveTime()==0 ? "am": "pm");
            Map<String,Object> param = new HashMap<String,Object>(){{
                put("hosname",orderInfo.getHosname());
                put("title", orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle());
                put("reserveDate", reserveDate);
                put("name", orderInfo.getPatientName());
            }};
            msmVo.setParam(param);
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
    }

    @Override
    public Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo) {
        Map<String, Object> map = new HashMap<>();

        List<OrderCountVo> orderCountVoList
                = baseMapper.selectOrderCount(orderCountQueryVo);
        //date list
        List<String> dateList
                =orderCountVoList.stream().map(OrderCountVo::getReserveDate).collect(Collectors.toList());
        //count list
        List<Integer> countList
                =orderCountVoList.stream().map(OrderCountVo::getCount).collect(Collectors.toList());
        map.put("dateList", dateList);
        map.put("countList", countList);
        return map;
    }


    private OrderInfo packOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
        return orderInfo;
    }
}