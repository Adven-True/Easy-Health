package com.atguigu.yygh.user.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserInfoApiController{

	@Autowired
	private UserInfoService userInfoService;


	@PostMapping("login")
	public Result login(@RequestBody LoginVo loginVo){
		Map<String, Object> info = userInfoService.loginUser(loginVo);
		return Result.ok(info);
	}


	@PostMapping("auth/userAuth")
	public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request){

		userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
		return Result.ok();
	}


	@GetMapping("auth/getUserInfo")
	public Result getUserInfo(HttpServletRequest request){
		Long userId = AuthContextHolder.getUserId(request);
		UserInfo userInfo = userInfoService.getById(userId);
		return Result.ok(userInfo);
	}
}
