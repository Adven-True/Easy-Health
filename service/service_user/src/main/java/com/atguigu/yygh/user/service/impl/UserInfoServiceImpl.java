package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.enums.AuthStatusEnum;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>implements UserInfoService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private PatientService patientService;


	@Override
	public Map<String, Object> loginUser(LoginVo loginVo) {

		String mail = loginVo.getMail();
		String code = loginVo.getCode();
		String openid = loginVo.getOpenid();

		if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(code)) {
			throw new YyghException(ResultCodeEnum.PARAM_ERROR);
		}
		String mailCode = redisTemplate.opsForValue().get(mail);
		if (!code.equals(mailCode)) {
			throw new YyghException(ResultCodeEnum.CODE_ERROR);
		}

		UserInfo userInfo = null;
		if(!StringUtils.isEmpty(openid)) {
			userInfo = this.selectWxInfoOpenId(openid);
			if (StringUtils.isEmpty(userInfo.getMail())){
				userInfo.setMail(mail);
				this.updateById(userInfo);
			}
//			if(null == userInfo) {
//				userInfo.setMail(loginVo.getMail());
//				this.updateById(userInfo);
//			} else {
//				throw new YyghException(ResultCodeEnum.DATA_ERROR);
//			}
		}


		if (null==userInfo) {
			QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
			wrapper.eq("mail", mail);
			userInfo = baseMapper.selectOne(wrapper);
			if (userInfo == null) {
				//register with email
				userInfo = new UserInfo();
				userInfo.setMail(mail);
				userInfo.setIsDeleted(0);
				userInfo.setStatus(1);
				baseMapper.insert(userInfo);
			}
		}
		//locked?
		if (userInfo.getStatus() == 0) {
			throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
		}

		//return token
		Map<String, Object> map = new HashMap();
		String name = userInfo.getName();
		if (StringUtils.isEmpty(name)) {
			name = userInfo.getNickName();
		}
		if (StringUtils.isEmpty(name)) {
			name = userInfo.getMail();
		}
		map.put("name", name);
		// jwt token
		String token = JwtHelper.createToken(userInfo.getId(), name);
		map.put("token", token);
		return map;
	}

	@Override
	public UserInfo selectWxInfoOpenId(String openid) {
		QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
		wrapper.eq("openid",openid);
		UserInfo userInfo = baseMapper.selectOne(wrapper);
		return userInfo;
	}

	@Override
	public void userAuth(Long userId, UserAuthVo userAuthVo) {

		UserInfo userInfo = baseMapper.selectById(userId);

		userInfo.setName(userAuthVo.getName());

		userInfo.setCertificatesType(userAuthVo.getCertificatesType());
		userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
		userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
		userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());

		baseMapper.updateById(userInfo);
	}

	//user list
	@Override
	public Page<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
		String name = userInfoQueryVo.getKeyword();//name
		Integer status = userInfoQueryVo.getStatus();//status
		Integer authStatus = userInfoQueryVo.getAuthStatus();//verification status
		String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();//start time
		String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();//end time
		//null cases
		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(name)){
			queryWrapper.like("name",name);
		}
		if (!StringUtils.isEmpty(status)){
			queryWrapper.like("status",status);
		}
		if (!StringUtils.isEmpty(authStatus)){
			queryWrapper.like("auth_status",authStatus);
		}
		if (!StringUtils.isEmpty(createTimeBegin)){
			queryWrapper.ge("create_time",createTimeBegin);
		}
		if (!StringUtils.isEmpty(createTimeEnd)){
			queryWrapper.le("create_time",createTimeEnd);
		}
		Page<UserInfo> userInfoPage = baseMapper.selectPage(pageParam, queryWrapper);

		userInfoPage.getRecords().stream().forEach(userInfo -> {
			packageUserInfo(userInfo);
		});
		return userInfoPage;
	}


	@Override
	public void lock(Long userId, Integer status) {

		if (status == 0 || status == 1){
			UserInfo userInfo = baseMapper.selectById(userId);
			userInfo.setStatus(status);
			baseMapper.updateById(userInfo);
		}
	}


	@Override
	public Map<String, Object> show(Long userId) {
		Map<String,Object> map = new HashMap<>();

		UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
		map.put("userInfo",userInfo);

		List<Patient> patientList = patientService.findAllByUserId(userId);
		map.put("patientList",patientList);
		return map;
	}


	@Override
	public void approval(Long userId, Integer authStatus) {
		if(authStatus ==2 || authStatus ==-1) {
			UserInfo userInfo = baseMapper.selectById(userId);
			userInfo.setAuthStatus(authStatus);
			baseMapper.updateById(userInfo);
		}
	}

	private UserInfo packageUserInfo(UserInfo userInfo) {
		Integer authStatus = userInfo.getAuthStatus();
		String statusString = userInfo.getStatus()==0?"lock" : "normal";
		userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(authStatus));
		userInfo.getParam().put("statusString",statusString);
		return userInfo;
	}


}


