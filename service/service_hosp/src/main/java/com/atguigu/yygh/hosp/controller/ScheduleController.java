package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

	@Autowired
	private ScheduleService scheduleService;

	//
	@ApiOperation(value = "findSchedulerule")
	@GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
	public Result getScheduleRule(@PathVariable("page") long page, @PathVariable("limit") long limit,
								  @PathVariable("hoscode") String hoscode, @PathVariable("depcode") String depcode) {
		Map<String, Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
		return Result.ok(map);
	}
	//
	@ApiOperation(value = "findScheduleInfo")
	@GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
	public Result getScheduleDetail(@PathVariable("hoscode") String hoscode,
									@PathVariable("depcode") String depcode,
									@PathVariable("workDate") String workDate){
		List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
		return Result.ok(list);
	}


}
