package com.atguigu.yygh.hosp.service.impl;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {


	@Override
	public String getSignKey(String hosCode) {
		QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
		wrapper.eq("hoscode",hosCode);
		HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
		if (hospitalSet!=null){
			return hospitalSet.getSignKey();
		}else {
			return "";
		}

	}


	@Override
	public SignInfoVo getSignInfoVo(String hoscode) {
		QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
		wrapper.eq("hoscode",hoscode);
		HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
		if(null == hospitalSet) {
			throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
		}
		SignInfoVo signInfoVo = new SignInfoVo();
		signInfoVo.setApiUrl(hospitalSet.getApiUrl());
		signInfoVo.setSignKey(hospitalSet.getSignKey());
		return signInfoVo;
	}
}
