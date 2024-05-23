package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {


	void save(Map<String, Object> paramMap);

	Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

	void remove(String hoscode, String hosScheduleId);


	Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);


	List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);


	Map<String, Object> getBookingScheduleRule(int page, int limit, String hoscode, String depcode);


	Schedule getById(String id);


	Schedule getScheduleByHosScheduleId(String id);


	ScheduleOrderVo getScheduleOrderVo(String scheduleId);

	/**
	 * edit schedule
	 */
	void update(Schedule schedule);
}
