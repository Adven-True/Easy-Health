package com.atguigu.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.ConstantPropertiesUtil;
import com.atguigu.yygh.user.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private RedisTemplate redisTemplate;


	@GetMapping("getLoginParam")
	@ResponseBody
	public Result genQrConnect(HttpSession session) throws UnsupportedEncodingException {

		Map<String, Object> map = new HashMap<>();
		map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
		map.put("scope","snsapi_login");
		String wxOpenRedirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
		//wxOpenRedirectUrl = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
		map.put("redirect_uri",wxOpenRedirectUrl);
		map.put("state",System.currentTimeMillis()+"");
		return Result.ok(map);

	}

	@GetMapping("callback")
	public String callback(String code,String state) {

		System.out.println("code:" + code);

		StringBuffer baseAccessTokenUrl = new StringBuffer()
				.append("https://api.weixin.qq.com/sns/oauth2/access_token")
				.append("?appid=%s")
				.append("&secret=%s")
				.append("&code=%s")
				.append("&grant_type=authorization_code");

		String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
				ConstantPropertiesUtil.WX_OPEN_APP_ID,
				ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
				code);


		try {
			String accesstokenInfo = HttpClientUtils.get(accessTokenUrl);
			System.out.println("accesstokenInfo:" + accesstokenInfo);
			JSONObject jsonObject = JSONObject.parseObject(accesstokenInfo);
			String access_token = jsonObject.getString("access_token");
			String openid = jsonObject.getString("openid");

			//base on openid
			UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
			if (userInfo == null) {//no data
				//3.use openid and access_token，get user info
				String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
						"?access_token=%s" +
						"&openid=%s";
				String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
				String resultInfo = HttpClientUtils.get(userInfoUrl);
				System.out.println(resultInfo);
				JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
				//parse
				String nickname = resultUserInfoJson.getString("nickname");  //nickname
				String headimgurl = resultUserInfoJson.getString("headimgurl");  //avatar

				//add to database
				userInfo = new UserInfo();
				userInfo.setOpenid(openid);
				userInfo.setNickName(nickname);
				userInfo.setStatus(1);
				userInfo.setAuthStatus(2);
				userInfoService.save(userInfo);
			}


			//return name and token string
			Map<String, Object> map = new HashMap<>();
			String name = userInfo.getName();
			if (StringUtils.isEmpty(name)) {
				name = userInfo.getNickName();
			}
			if (StringUtils.isEmpty(name)) {
				name = userInfo.getMail();
			}
			map.put("name", name);

			if (StringUtils.isEmpty(userInfo.getMail())) {
				map.put("openid", "");
			} else {
				map.put("openid", userInfo.getOpenid());
			}
			String token = JwtHelper.createToken(userInfo.getId(), name);
			map.put("token", token);
			//redirect
			return "redirect:" + ConstantPropertiesUtil.YYGH_BASE_URL +
					"/weixin/callback?token=" + map.get("token") +
					"&openid=" + map.get("openid") +
					"&name=" + URLEncoder.encode((String) map.get("name"), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
