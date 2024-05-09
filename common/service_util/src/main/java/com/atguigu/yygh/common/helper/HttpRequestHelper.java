package com.atguigu.yygh.common.helper;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import com.atguigu.yygh.common.utils.MD5;

@Slf4j
public class HttpRequestHelper {

    public static void main(String[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("d", "4");
        paramMap.put("b", "2");
        paramMap.put("c", "3");
        paramMap.put("a", "1");
        paramMap.put("timestamp", getTimestamp());
        log.info(getSign(paramMap, "111111111"));
    }

    /**
     *
     * @param paramMap
     * @return
     */
    public static Map<String, Object> switchMap(Map<String, String[]> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            resultMap.put(param.getKey(), param.getValue()[0]);
        }
        return resultMap;
    }

    /**
     * The request data obtains a signature
     * @param paramMap
     * @param signKey
     * @return
     */
    public static String getSign(Map<String, Object> paramMap, String signKey) {
        if(paramMap.containsKey("sign")) {
            paramMap.remove("sign");
        }
        String md5Str = MD5.encrypt(signKey);
//        TreeMap<String, Object> sorted = new TreeMap<>(paramMap);
//        StringBuilder str = new StringBuilder();
//        for (Map.Entry<String, Object> param : sorted.entrySet()) {
//            str.append(param.getValue()).append("|");
//        }
//        str.append(signKey);
//        log.info("Before encryption：" + str.toString());
//        String md5Str = MD5.encrypt(str.toString());
//        log.info("After encryption：" + md5Str);
        return md5Str;
    }

    /**
     * Signature verification
     * @param paramMap
     * @param signKey
     * @return
     */
    public static boolean isSignEquals(Map<String, Object> paramMap, String signKey) {
        String sign = (String)paramMap.get("sign");
        String md5Str = getSign(paramMap, signKey);
        if(!sign.equals(md5Str)) {
            return false;
        }
        return true;
    }

    /**
     * Get timestamp
     * @return
     */
    public static long getTimestamp() {
        return new Date().getTime();
    }

    /**
     * Encapsulate synchronous request.
     * @param paramMap
     * @param url
     * @return
     */
    public static JSONObject sendRequest(Map<String, Object> paramMap, String url){
        String result = "";
        try {
            //Encapsulate POST parameters
            StringBuilder postdata = new StringBuilder();
            for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                postdata.append(param.getKey()).append("=")
                        .append(param.getValue()).append("&");
            }
            log.info(String.format("--> request：post data %1s", postdata));
            byte[] reqData = postdata.toString().getBytes("utf-8");
            byte[] respdata = HttpUtil.doPost(url,reqData);
            result = new String(respdata);
            log.info(String.format("--> response：result data %1s", result));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.parseObject(result);
    }
}
