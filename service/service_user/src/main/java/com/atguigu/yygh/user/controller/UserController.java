package com.atguigu.yygh.user.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class UserController {

	@Autowired
	private UserInfoService userInfoService;


	@GetMapping("{page}/{limit}")
	public Result list(@PathVariable Long page, @PathVariable Long limit, UserInfoQueryVo userInfoQueryVo){
		Page<UserInfo> pageParam = new Page<>(page,limit);
		Page<UserInfo> pageModel =  userInfoService.selectPage(pageParam,userInfoQueryVo);
		return Result.ok(pageModel);
	}


	@GetMapping("lock/{userId}/{status}")
	public Result lock(@PathVariable Long userId,@PathVariable Integer status){
		userInfoService.lock(userId,status);
		return Result.ok();
	}


	@GetMapping("show/{userId}")
	public Result show(@PathVariable Long userId) {
		Map<String,Object> map = userInfoService.show(userId);
		return Result.ok(map);
	}


	@GetMapping("approval/{userId}/{authStatus}")
	public Result approval(@PathVariable Long userId,@PathVariable Integer authStatus) {
		userInfoService.approval(userId,authStatus);
		return Result.ok();
	}
}
