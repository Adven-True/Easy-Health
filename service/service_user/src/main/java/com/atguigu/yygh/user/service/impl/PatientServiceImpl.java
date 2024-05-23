package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.enums.DictEnum;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.mapper.PatientMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

	@Autowired
	private DictFeignClient dictFeignClient;

	@Override
	public List<Patient> findAllByUserId(Long userId) {
		QueryWrapper<Patient> wrapper = new QueryWrapper<>();
		wrapper.eq("user_id",userId);
		List<Patient> patientList = baseMapper.selectList(wrapper);

		patientList.forEach(this::packagePatient);
		return patientList;
	}

	@Override
	public Patient getByPatientId(Long id) {
		Patient patient = getById(id);
		packagePatient(patient);
		return patient;
	}

	private void packagePatient(Patient patient) {
		String certificatesTypeString =
		dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());//contact ID

		String contactsCertificatesTypeString =
		dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
		//province
		String provinceString = dictFeignClient.getName(patient.getProvinceCode());
		//city
		String cityString = dictFeignClient.getName(patient.getCityCode());
		//district
		String districtString = dictFeignClient.getName(patient.getDistrictCode());
		patient.getParam().put("certificatesTypeString", certificatesTypeString);
		patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
		patient.getParam().put("provinceString", provinceString);
		patient.getParam().put("cityString", cityString);
		patient.getParam().put("districtString", districtString);
		patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
	}
}
