package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	//
	@Override
	public void save(Map<String, Object> paramMap) {
		// paramMap to Department
		String jsonString = JSONObject.toJSONString(paramMap);
		Department department = JSONObject.parseObject(jsonString,Department.class);

		Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());

		if (departmentExist!=null){
			departmentExist.setUpdateTime(new Date());
			departmentExist.setIsDeleted(0);
			departmentRepository.save(departmentExist);
		}else {
			department.setCreateTime(new Date());
			department.setUpdateTime(new Date());
			departmentRepository.save(department);
		}
	}

	@Override
	public Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {

		Pageable pageable = PageRequest.of(page-1, limit);
		//create Example object
		ExampleMatcher matcher  = ExampleMatcher.matching()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withIgnoreCase(true);

		Department department = new Department();
		BeanUtils.copyProperties(departmentQueryVo,department);


		Example<Department> example = Example.of(department,matcher);
		Page<Department> result = departmentRepository.findAll(example, pageable);
		return result;
	}

	@Override
	public void remove(String hoscode, String depcode) {

		Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
		if (department==null){
			return;
		}
		departmentRepository.deleteById(department.getId());
	}


	@Override
	public List<DepartmentVo> findDeptTree(String hoscode) {
		//create list
		List<DepartmentVo> result = new ArrayList<>();

		Department departmentQuery = new Department();
		departmentQuery.setHoscode(hoscode);
		Example example = Example.of(departmentQuery);
		//all department list info
		List<Department> departmentList = departmentRepository.findAll(example);
		Map<String, List<Department>> departmentMap =
				departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
		//traverse map departmentMap
		for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
			//big depart code
			String bigcode = entry.getKey();
			//all data
			List<Department> bigdepartment = entry.getValue();

			DepartmentVo departmentVo = new DepartmentVo();

			departmentVo.setDepcode(bigcode);
			departmentVo.setDepname(bigdepartment.get(0).getDepname());

			List<DepartmentVo> children = new ArrayList<>();
			for (Department department : bigdepartment){
				DepartmentVo departmentVo1 = new DepartmentVo();
				departmentVo1.setDepcode(department.getDepcode());
				departmentVo1.setDepname(department.getDepname());
				//Encapsulate into the list collection
				children.add(departmentVo1);
			}
			departmentVo.setChildren(children);

			result.add(departmentVo);
		}

		return result;
	}

	@Override
	public String getDepName(String hoscode, String depcode) {
		Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
		if (department!=null){
			return department.getDepname();
		}
		return null;
	}

	@Override
	public Department getDepartment(String hoscode, String depcode) {
		Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
		if (department!=null){
			return department;
		}
		return null;
	}
}
