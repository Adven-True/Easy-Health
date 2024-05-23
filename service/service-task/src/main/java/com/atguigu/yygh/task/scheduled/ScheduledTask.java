package com.atguigu.yygh.task.scheduled;

import com.atguigu.yygh.rabbit.constant.MqConst;
import com.atguigu.yygh.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lijian
 * @create 2021-05-05 17:03
 */

@Component
@EnableScheduling  //Enable scheduled task operations
public class ScheduledTask {
    @Autowired
    private RabbitService rabbitService;


    //@Scheduled(cron = "0 0 1 * * ?")  at 8:00 am every day
    @Scheduled(cron = "0/30 * * * * ?")  //every 30 s
    public void taskPatient() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, "");
    }
}

