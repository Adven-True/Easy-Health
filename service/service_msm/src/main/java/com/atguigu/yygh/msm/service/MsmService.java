package com.atguigu.yygh.msm.service;

import com.atguigu.yygh.vo.msm.MsmVo;
import org.springframework.stereotype.Service;

public interface MsmService {
    //send verification code
    boolean send(String mail, String code);

    //use message queue
    boolean send(MsmVo msmVo);
}