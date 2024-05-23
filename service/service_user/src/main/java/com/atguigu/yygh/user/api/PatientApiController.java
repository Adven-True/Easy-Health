package com.atguigu.yygh.user.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.service.PatientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

	@Autowired
	private PatientService patientService;


	@GetMapping("auth/findAll")
	public Result findAll(HttpServletRequest request){

		Long userId = AuthContextHolder.getUserId(request);
		List<Patient> list = patientService.findAllByUserId(userId);
		return Result.ok(list);
	}


	@PostMapping("auth/save")
	public Result savePatient(@RequestBody Patient patient,HttpServletRequest request){
		Long userId = AuthContextHolder.getUserId(request);
		patient.setUserId(userId);
		patientService.save(patient);
		return Result.ok();
	}


	@GetMapping("auth/get/{id}")
	public Result getById(@PathVariable("id") Long id){
		Patient patient = patientService.getByPatientId(id);
		return Result.ok(patient);
	}


	@PostMapping("auth/update")
	public Result updatePatient(@RequestBody Patient patient){
		patientService.updateById(patient);
		return Result.ok();
	}

	@DeleteMapping("auth/remove/{id}")
	public Result removeById(@PathVariable("id") Long id){
		patientService.removeById(id);
		return Result.ok();
	}

	@ApiOperation(value = "get patient")
	@GetMapping("inner/get/{id}")
	public Patient getPatientOrder(
			@ApiParam(name = "id", value = "patient id", required = true)
			@PathVariable("id") Long id) {
		return patientService.getById(id);
	}
}
