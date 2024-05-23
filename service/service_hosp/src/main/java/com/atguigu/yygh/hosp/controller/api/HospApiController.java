package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.atguigu.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/hosp/hospital")
@Api("HospApiController")
public class HospApiController {

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private HospitalSetService hospitalSetService;

	@ApiOperation("hosp list")
	@GetMapping("findHospList/{page}/{limit}")
	public Result findHospList(@PathVariable Integer page, @PathVariable Integer limit, HospitalQueryVo hospitalQueryVo){
		Page<Hospital> result = hospitalService.selectHospitalPage(page, limit, hospitalQueryVo);
		return Result.ok(result);
	}

	@ApiOperation("fingByName")
	@GetMapping("findByHosName/{hosname}")
	public Result findByHosName(@PathVariable String hosname){
		List<Hospital> list = hospitalService.findByHosName(hosname);
		return Result.ok(list);
	}

	@ApiOperation("findDepartByHospId")
	@GetMapping("department/{hoscode}")
	public Result index(@PathVariable String hoscode){
		List<DepartmentVo> departmentVoList = departmentService.findDeptTree(hoscode);
		return Result.ok(departmentVoList);
	}

	@ApiOperation("findOrderByHospId")
	@GetMapping("findHospDetail/{hoscode}")
	public Result findHospDetail(@PathVariable String hoscode){
		Map<String, Object> map = hospitalService.item(hoscode);
		return Result.ok(map);
	}

	@ApiOperation(value = "findAvailable")
	@GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
	public Result getBookingScheduleRule(
			@ApiParam(name = "page", value = "current page number", required = true)
			@PathVariable Integer page,
			@ApiParam(name = "limit", value = "records on each page", required = true)
			@PathVariable Integer limit,
			@ApiParam(name = "hoscode", value = "hospital code", required = true)
			@PathVariable String hoscode,
			@ApiParam(name = "depcode", value = "department code", required = true)
			@PathVariable String depcode) {
		return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
	}

	@ApiOperation(value = "findScheduleData")
	@GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
	public Result findScheduleList(
			@ApiParam(name = "hoscode", value = "hospital code", required = true)
			@PathVariable String hoscode,
			@ApiParam(name = "depcode", value = "department code", required = true)
			@PathVariable String depcode,
			@ApiParam(name = "workDate", value = "scheduleDate", required = true)
			@PathVariable String workDate) {
		return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
	}

	@ApiOperation(value = "findDataByScheduleId")
	@GetMapping("getSchedule/{scheduleId}")
	public Result getSchedule(
			@ApiParam(name = "scheduleId", value = "schedule id", required = true)
			@PathVariable String scheduleId) {
		return Result.ok(scheduleService.getById(scheduleId));
	}

	//
	@ApiOperation(value = "fingOrderByScheduleId")
	@GetMapping("inner/getScheduleOrderVo/{scheduleId}")
	public ScheduleOrderVo getScheduleOrderVo(
			@ApiParam(name = "scheduleId", value = "schedule id", required = true)
			@PathVariable("scheduleId") String scheduleId) {
		return scheduleService.getScheduleOrderVo(scheduleId);
	}

	//
	@ApiOperation(value = "findHospSignData")
	@GetMapping("inner/getSignInfoVo/{hoscode}")
	public SignInfoVo getSignInfoVo(
			@ApiParam(name = "hoscode", value = "hospital code", required = true)
			@PathVariable("hoscode") String hoscode) {
		return hospitalSetService.getSignInfoVo(hoscode);
	}



}
