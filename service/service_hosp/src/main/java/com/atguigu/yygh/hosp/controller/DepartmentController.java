package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	//
	@ApiOperation(value = "findDepartmentList")
	@GetMapping("getDeptList/{hoscode}")
	public Result getDeptList(@PathVariable("hoscode") String hoscode){
		List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
		return Result.ok(list);
	}
}
