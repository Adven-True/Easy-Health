package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
	void save(Map<String, Object> paramMap);


	Hospital getByHoscode(String hosCode);


	Page<Hospital> selectHospitalPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);


	void updateStatus(String id, Integer status);


	Map<String, Object> getHospById(String id);


	String getHospName(String hoscode);


	List<Hospital> findByHosName(String hosname);


	Map<String, Object> item(String hoscode);
}
