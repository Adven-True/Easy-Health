package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "hospConfigManage")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {



	@Autowired
	private HospitalSetService hospitalSetService;


	/**
	 *
	 * @return
	 */
	@ApiOperation(value = "findAllHospConfig")
	@GetMapping("findAll")
	public Result findAllHospitalSet(){

		List<HospitalSet> list = hospitalSetService.list();
		return Result.ok(list);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "logicDeleteHospInfo")
	@DeleteMapping("{id}")
	public Result removeHospSetById(@PathVariable("id") Long id){
		boolean flag = hospitalSetService.removeById(id);
		if (flag){
			return Result.ok();
		}else {
			return Result.fail();
		}
	}


	/**
	 *
	 *      @RequestBody(required = false)
	 * @param current:current page
	 * @param limit
	 * @param hospitalSetQueryVo
	 * @return
	 */
	@ApiOperation(value = "conditional query with pagination")
	@PostMapping("findPageHospSet/{current}/{limit}")
	public Result findPageHospSet(@PathVariable("current") long current,
								  @PathVariable("limit") long limit,
								  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo
								  ){


		Page<HospitalSet> page = new Page<>(current,limit);
		QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(hospitalSetQueryVo)) {
			String hosname = hospitalSetQueryVo.getHosname();
			String hoscode = hospitalSetQueryVo.getHoscode();

			if (!StringUtils.isEmpty(hosname)) {
				wrapper.like("hosname", hosname);
			}
			if (!StringUtils.isEmpty(hoscode)) {
				wrapper.eq("hoscode", hoscode);
			}
		}


		Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);

		return Result.ok(hospitalSetPage);
	}

	/**
	 *
	 * @param hospitalSet
	 * @return
	 */
	@PostMapping("saveHospitalSet")
	public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
		//1 available 0 not available
		hospitalSet.setStatus(1);
		//key
		Random random = new Random();
		hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
		//call service
		boolean flag = hospitalSetService.save(hospitalSet);
		if (flag){
			return Result.ok();
		}else {
			return Result.fail();
		}

	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("getHospitalSetById/{id}")
	public Result getHospitalSetById(@PathVariable("id") Long  id){
		HospitalSet hospitalSet = hospitalSetService.getById(id);
		return Result.ok(hospitalSet);
	}

	/**
	 *
	 * @param hospitalSet
	 * @return
	 */
	@PostMapping("updateHospitalSet")
	public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
		boolean flag = hospitalSetService.updateById(hospitalSet);
		if (flag){
			return Result.ok();
		}else {
			return Result.fail();
		}
	}


	/**
	 *
	 * @param idList
	 * @return
	 */
	@DeleteMapping("batchRemove")
	public Result batchRemoveHospitalSet(@RequestBody List<Long> idList){
		boolean flag = hospitalSetService.removeByIds(idList);
		if (flag){
			return Result.ok();
		}else {
			return Result.fail();
		}
	}


	/**
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@PutMapping("lockHospitalSet/{id}/{status}")
	public Result lockHospitalSet(@PathVariable Long id,
								  @PathVariable Integer status){
		//
		HospitalSet hospitalSet = hospitalSetService.getById(id);
		//
		hospitalSet.setStatus(status);
		boolean flag = hospitalSetService.updateById(hospitalSet);
		if (flag){
			return Result.ok();
		}else {
			return Result.fail();
		}

	}


	/**
	 *
	 * @param id
	 * @return
	 */
	@PutMapping("sendKey/{id}")
	public Result lockHospitalSet(@PathVariable Long id){
		//
		HospitalSet hospitalSet = hospitalSetService.getById(id);
		String signKey = hospitalSet.getSignKey();
		String hoscode = hospitalSet.getHoscode();
		//TODO send message
		return Result.ok();
	}

}
