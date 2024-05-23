package com.atguigu.yygh.msm.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.RandomUtil;
import com.atguigu.yygh.msm.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Api(tags = "send message")
@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //email verification code
    @ApiOperation(value = "send email verification code")
    @GetMapping("send/{mail}")
    public Result sendCode(@PathVariable String mail) {

        String code = redisTemplate.opsForValue().get(mail);
        if(!StringUtils.isEmpty(code)) {
            return Result.ok();
        }
        //no data in redis
        // generate code
        code = RandomUtil.getSixBitRandom();

        boolean isSend = msmService.send(mail,code);
        //put code in redis and config expire time
        if(isSend) {
            redisTemplate.opsForValue().set(mail,code,10, TimeUnit.MINUTES);
            return Result.ok();
        } else {
            return Result.fail().message("email send fail");
        }
    }
}