package com.atguigu.yygh.common.utils;

import com.atguigu.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

public class AuthContextHolder {
	//Get current user ID
	public static Long getUserId(HttpServletRequest request) {
		//Get token from header
		String token = request.getHeader("token");
		//Get user ID from token in JWT
		Long userId = JwtHelper.getUserId(token);
		return userId;
	}
	//Get current user name
	public static String getUserName(HttpServletRequest request) {
		//Get token from header
		String token = request.getHeader("token");
		//Get user ID from token in JWT
		String userName = JwtHelper.getUserName(token);
		return userName;
	}
}
