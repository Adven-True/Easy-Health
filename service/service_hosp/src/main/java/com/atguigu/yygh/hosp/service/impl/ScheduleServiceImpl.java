package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.BookingRule;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private DepartmentService departmentService;

	@Override
	public void save(Map<String, Object> paramMap) {
		String jsonString = JSONObject.toJSONString(paramMap);
		Schedule schedule = JSONObject.parseObject(jsonString, Schedule.class);

		Schedule scheduleExists = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());

		if (scheduleExists!=null){
			scheduleExists.setUpdateTime(new Date());
			scheduleExists.setIsDeleted(0);

			scheduleExists.setStatus(1);
			scheduleRepository.save(scheduleExists);
		}else {
			schedule.setCreateTime(new Date());
			schedule.setUpdateTime(new Date());
			schedule.setIsDeleted(0);
			//available
			schedule.setStatus(1);
			scheduleRepository.save(schedule);
		}

	}

	@Override
	public Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
		Schedule schedule = new Schedule();
		BeanUtils.copyProperties(scheduleQueryVo,schedule);
		schedule.setStatus(1);
		schedule.setIsDeleted(0);

		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withIgnoreCase(true);
		Example<Schedule> example = Example.of(schedule, matcher);

		Pageable pageable = PageRequest.of(page - 1, limit);
		Page<Schedule> all = scheduleRepository.findAll(example, pageable);
		return all;
	}

	@Override
	public void remove(String hoscode, String hosScheduleId) {

		Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
		if (schedule!=null){
			scheduleRepository.deleteById(schedule.getId());
		}
	}


	@Override
	public Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode) {

		Object o;
		Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

		Aggregation agg = Aggregation.newAggregation(
				Aggregation.match(criteria),
				Aggregation.group("workDate")
				.first("workDate").as("workDate")
				.count().as("docCount")
				.sum("reservedNumber").as("reservedNumber")
				.sum("availableNumber").as("availableNumber"),
				//sort
				Aggregation.sort(Sort.Direction.DESC,"workDate"),
				//pagenation
				Aggregation.skip((page-1)*limit),
				Aggregation.limit(limit)
		);

		AggregationResults<BookingScheduleRuleVo> aggResult = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
		List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResult.getMappedResults();

		Aggregation totalsAgg = Aggregation.newAggregation(
				Aggregation.match(criteria),
				Aggregation.group("workDate")
		);
		AggregationResults<BookingScheduleRuleVo> totalAggregate =
				mongoTemplate.aggregate(totalsAgg, Schedule.class, BookingScheduleRuleVo.class);
		int total = totalAggregate.getMappedResults().size();


		for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList){
			bookingScheduleRuleVo.setDayOfWeek(getDayOfWeek(new DateTime(bookingScheduleRuleVo.getWorkDate())));
		}

		String hosName = hospitalService.getHospName(hoscode);

		HashMap<String, String> baseMap = new HashMap<>();
		baseMap.put("hosname",hosName);


		Map<String, Object> result = new HashMap<>();
		result.put("bookingScheduleRuleList",bookingScheduleRuleVoList);
		result.put("total",total);
		result.put("baseMap",baseMap);
		return result;
	}


	@Override
	public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
		Date date = new DateTime(workDate).toDate();
		List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode,depcode, date);

		scheduleList.forEach(this::packageSchedule);
		return scheduleList;
	}


	private void packageSchedule(Schedule schedule) {

		schedule.getParam().put("hosname",hospitalService.getHospName(schedule.getHoscode()));

		schedule.getParam().put("depname",departmentService.getDepName(schedule.getHoscode(),schedule.getDepcode()));

		schedule.getParam().put("dayOfWeek",getDayOfWeek(new DateTime(schedule.getWorkDate())));
	}

	@Override
	public Map<String, Object> getBookingScheduleRule(int page, int limit, String hoscode, String depcode) {
		Map<String,Object> result = new HashMap<>();

		Hospital hospital = hospitalService.getByHoscode(hoscode);
		if(hospital == null){
			throw new YyghException(ResultCodeEnum.DATA_ERROR);
		}
		BookingRule bookingRule = hospital.getBookingRule();


		IPage iPage = this.getListDate(page,limit,bookingRule);

		List<Date> dateList = iPage.getRecords();

		Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(dateList);
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.match(criteria),
				Aggregation.group("workDate").first("workDate").as("workDate")
						.count().as("docCount")
						.sum("availableNumber").as("availableNumber")
						.sum("reservedNumber").as("reservedNumber")
		);

		AggregationResults<BookingScheduleRuleVo> aggregateResult = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
		List<BookingScheduleRuleVo> scheduleVoList = aggregateResult.getMappedResults();

		//merge data
		Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
		if(!CollectionUtils.isEmpty(scheduleVoList)) {
			scheduleVoMap = scheduleVoList.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, BookingScheduleRuleVo -> BookingScheduleRuleVo));
		}


		List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
		for(int i=0, len=dateList.size(); i<len; i++) {
			Date date = dateList.get(i);

			BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
			if(bookingScheduleRuleVo == null){
				bookingScheduleRuleVo = new BookingScheduleRuleVo();

				bookingScheduleRuleVo.setDocCount(0);

				bookingScheduleRuleVo.setAvailableNumber(-1);
			}

			bookingScheduleRuleVo.setWorkDate(date);
			bookingScheduleRuleVo.setWorkDateMd(date);

			String dayOfWeek = this.getDayOfWeek(new DateTime(date));
			bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);


			if(i == len-1 && page == iPage.getPages()) {
				bookingScheduleRuleVo.setStatus(1);
			} else {
				bookingScheduleRuleVo.setStatus(0);
			}

			if(i == 0 && page == 1) {
				DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
				if(stopTime.isBeforeNow()) {

					bookingScheduleRuleVo.setStatus(-1);
				}
			}
			bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
		}

		result.put("bookingScheduleList", bookingScheduleRuleVoList);
		result.put("total", iPage.getTotal());

		Map<String, String> baseMap = new HashMap<>();

		baseMap.put("hosname", hospitalService.getHospName(hoscode));

		Department department =departmentService.getDepartment(hoscode, depcode);

		baseMap.put("bigname", department.getBigname());

		baseMap.put("depname", department.getDepname());
		//month
		baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
		//release available slots
		baseMap.put("releaseTime", bookingRule.getReleaseTime());
		//stop making appointment
		baseMap.put("stopTime", bookingRule.getStopTime());
		result.put("baseMap", baseMap);
		return result;
	}

	@Override
	public Schedule getById(String id) {
		Schedule schedule = scheduleRepository.findById(id).get();
		packageSchedule(schedule);
		return schedule;
	}

	@Override
	public Schedule getScheduleByHosScheduleId(String id){
		Schedule schedule = scheduleRepository.getScheduleByHosScheduleId(id);
		packageSchedule(schedule);
		return schedule;
	}




	@Override
	public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
		ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();

		Schedule schedule = scheduleRepository.findById(scheduleId).get();
		if(null == schedule) {
			throw new YyghException(ResultCodeEnum.PARAM_ERROR);
		}


		Hospital hospital = hospitalService.getByHoscode(schedule.getHoscode());
		if(null == hospital) {
			throw new YyghException(ResultCodeEnum.DATA_ERROR);
		}
		BookingRule bookingRule = hospital.getBookingRule();
		if(null == bookingRule) {
			throw new YyghException(ResultCodeEnum.PARAM_ERROR);
		}

		scheduleOrderVo.setHoscode(schedule.getHoscode());
		scheduleOrderVo.setHosname(hospitalService.getHospName(schedule.getHoscode()));
		scheduleOrderVo.setDepcode(schedule.getDepcode());
		scheduleOrderVo.setDepname(departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
		scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
		scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
		scheduleOrderVo.setTitle(schedule.getTitle());
		scheduleOrderVo.setReserveDate(schedule.getWorkDate());
		scheduleOrderVo.setReserveTime(schedule.getWorkTime());
		scheduleOrderVo.setAmount(schedule.getAmount());


		int quitDay = bookingRule.getQuitDay();
		DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule.getQuitTime());
		scheduleOrderVo.setQuitTime(quitTime.toDate());


		DateTime startTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
		scheduleOrderVo.setStartTime(startTime.toDate());


		DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
		scheduleOrderVo.setEndTime(endTime.toDate());


		DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
		scheduleOrderVo.setStartTime(startTime.toDate());
		return scheduleOrderVo;
	}

	@Override
	public void update(Schedule schedule) {
		schedule.setUpdateTime(new Date());

		scheduleRepository.save(schedule);
	}


	private IPage<Date> getListDate(int page, int limit, BookingRule bookingRule) {

		DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());

		int cycle = bookingRule.getCycle();

		if(releaseTime.isBeforeNow()) cycle += 1;

		List<Date> dateList = new ArrayList<>();
		for (int i = 0; i < cycle; i++) {

			DateTime curDateTime = new DateTime().plusDays(i);
			String dateString = curDateTime.toString("yyyy-MM-dd");
			dateList.add(new DateTime(dateString).toDate());
		}

		List<Date> pageDateList = new ArrayList<>();
		int start = (page-1)*limit;
		int end = (page-1)*limit+limit;
		if(end >dateList.size()) end = dateList.size();
		for (int i = start; i < end; i++) {
			pageDateList.add(dateList.get(i));
		}
		IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page(page, 7, dateList.size());
		iPage.setRecords(pageDateList);
		return iPage;
	}

	private DateTime getDateTime(Date date, String timeString) {
		String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " "+ timeString;
		DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
		return dateTime;
	}

	/**
	 *
	 * @param dateTime
	 * @return
	 */
	private String getDayOfWeek(DateTime dateTime) {
		String dayOfWeek = "";
		switch (dateTime.getDayOfWeek()) {
			case DateTimeConstants.SUNDAY:
				dayOfWeek = "Sunday";
				break;
			case DateTimeConstants.MONDAY:
				dayOfWeek = "Monday";
				break;
			case DateTimeConstants.TUESDAY:
				dayOfWeek = "Tuesday";
				break;
			case DateTimeConstants.WEDNESDAY:
				dayOfWeek = "Wednesday";
				break;
			case DateTimeConstants.THURSDAY:
				dayOfWeek = "Thursday";
				break;
			case DateTimeConstants.FRIDAY:
				dayOfWeek = "Friday";
				break;
			case DateTimeConstants.SATURDAY:
				dayOfWeek = "Saturday";
			default:
				break;
		}
		return dayOfWeek;
	}
}
