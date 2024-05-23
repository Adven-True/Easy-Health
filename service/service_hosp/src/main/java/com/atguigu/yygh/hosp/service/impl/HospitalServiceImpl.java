package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private DictFeignClient dictFeignClient;


	@Override
	public void save(Map<String, Object> paramMap) {


		String mapString = JSONObject.toJSONString(paramMap);
		Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

		String hoscode = hospital.getHoscode();
		Hospital hospitalExit = hospitalRepository.getHospitalByHoscode(hoscode);


		if (hospitalExit==null){
			hospital.setStatus(0);
			hospital.setCreateTime(new Date());
			hospital.setUpdateTime(new Date());
			hospital.setIsDeleted(0);
			hospitalRepository.save(hospital);
		}else {
			hospital.setStatus(hospitalExit.getStatus());
			hospital.setCreateTime(hospitalExit.getCreateTime());
			hospital.setUpdateTime(new Date());
			hospital.setIsDeleted(0);
			hospitalRepository.save(hospital);

		}
	}

	@Override
	public Hospital getByHoscode(String hosCode) {
		Hospital hospital = hospitalRepository.getHospitalByHoscode(hosCode);
		return hospital;
	}

	@Override
	public Page<Hospital> selectHospitalPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {

		Pageable pageable = PageRequest.of(page-1, limit);

		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withIgnoreCase(true);


		Hospital hospital = new Hospital();
		BeanUtils.copyProperties(hospitalQueryVo,hospital);
		//create object
		Example<Hospital> example = Example.of(hospital,matcher);
		Page<Hospital> hospitalPage = hospitalRepository.findAll(example, pageable);

		hospitalPage.getContent().forEach(item->{
			setHospitalHosType(item);
		});
		return hospitalPage;
	}

	@Override
	public void updateStatus(String id, Integer status) {

		Hospital hospital = hospitalRepository.findById(id).get();
		hospital.setStatus(status);
		hospital.setUpdateTime(new Date());
		hospitalRepository.save(hospital);
	}

	@Override
	public Map<String, Object> getHospById(String id) {
		Map<String, Object> map = new HashMap<>();
		Hospital hospital = setHospitalHosType(hospitalRepository.findById(id).get());
		map.put("hospital",hospital);

		map.put("bookingRule",hospital.getBookingRule());

//		hospital.setBookingRule(null);
		return map;
	}

	@Override
	public String getHospName(String hoscode) {
		Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
		if (hospital!=null){
			return hospital.getHosname();
		}
		return null;
	}

	@Override
	public List<Hospital> findByHosName(String hosname) {
		List<Hospital> list = hospitalRepository.findHospitalByHosnameLike(hosname);
		return list;
	}

	@Override
	public Map<String, Object> item(String hoscode) {
		Map<String, Object> result = new HashMap<>();
		Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
		result.put("hospital",hospital);
		result.put("bookingRule",hospital.getBookingRule());
		hospital.setBookingRule(null);
		return result;
	}

	//获取查询医院等级信息
	private Hospital setHospitalHosType(Hospital hospital) {
		//查询省市区
		String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
		String cityString = dictFeignClient.getName(hospital.getCityCode());
		String districtString = dictFeignClient.getName(hospital.getDistrictCode());


		//查询医院类型
		String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());

		hospital.getParam().put("hostypeString",hostypeString);
		hospital.getParam().put("fullAddress",provinceString+cityString+districtString);
		return hospital;
	}
}
