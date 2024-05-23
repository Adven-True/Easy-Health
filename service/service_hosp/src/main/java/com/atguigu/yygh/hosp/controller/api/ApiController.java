package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private HospitalSetService hospitalSetService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ScheduleService scheduleService;


	@PostMapping("schedule/remove")
	public Result removeSchedule(HttpServletRequest request){

		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

		String hoscode = verifySignCode(paramMap);
		String hosScheduleId = (String) paramMap.get("hosScheduleId");

		scheduleService.remove(hoscode,hosScheduleId);
		return Result.ok();

	}


	@PostMapping("schedule/list")
	public Result findSchedule(HttpServletRequest request){

		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);


		int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
		int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

		String hoscode = verifySignCode(paramMap);
		String depcode = (String) paramMap.get("depcode");
		ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
		scheduleQueryVo.setHoscode(hoscode);
		scheduleQueryVo.setDepcode(depcode);
		Page<Schedule> schedulesModel = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
		return Result.ok(schedulesModel);
	}


	@PostMapping("saveSchedule")
	public Result saveSchedule(HttpServletRequest request){

		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

		verifySignCode(paramMap);

		scheduleService.save(paramMap);

		return Result.ok();
	}


	@PostMapping("department/remove")
	public Result removeDepartment(HttpServletRequest request){

		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);


		String hoscode = (String) paramMap.get("hoscode");

		String depcode = (String) paramMap.get("depcode");

		verifySignCode(paramMap);

		departmentService.remove(hoscode,depcode);

		return Result.ok();
	}


	@PostMapping("department/list")
	public Result findDepartment(HttpServletRequest request){

		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

//		String hoscode = (String) paramMap.get("hoscode");

		int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
		int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

		String hoscode = verifySignCode(paramMap);

		DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
		departmentQueryVo.setHoscode(hoscode);

		Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);

		return Result.ok(pageModel);
	}


	@PostMapping("saveDepartment")
	public Result saveDepartment(HttpServletRequest request){

		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
		verifySignCode(paramMap);

		departmentService.save(paramMap);
		return Result.ok();
	}



	@PostMapping("hospital/show")
	public Result getHospital(HttpServletRequest request){

		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);


		String hosCode = verifySignCode(paramMap);


		Hospital hospital = hospitalService.getByHoscode(hosCode);
		return Result.ok(hospital);
	}


	private String verifySignCode(Map<String, Object> paramMap) {


		String hosCode = (String) paramMap.get("hoscode");

		String hospSign = (String) paramMap.get("sign");

		String signKey = hospitalSetService.getSignKey(hosCode);

		if (!hospSign.equals(signKey)){
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}
		return hosCode;
	}


	@PostMapping("saveHospital")
	public Result saveHosp(HttpServletRequest request){

		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
		verifySignCode(paramMap);

		String logoData = (String) paramMap.get("logoData");
		logoData = logoData.replaceAll(" ","+");
		paramMap.put("logoData",logoData);

		hospitalService.save(paramMap);
		return Result.ok();
	}
}
