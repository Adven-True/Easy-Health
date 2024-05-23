package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
	Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

	Schedule getScheduleByHosScheduleId(String hosscheduleId);

	//Query scheduling details based on hospital number, department number, and work date
	List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
