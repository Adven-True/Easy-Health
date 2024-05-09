package com.atguigu.hospital.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.hospital.mapper.HospitalSetMapper;
import com.atguigu.hospital.mapper.ScheduleMapper;
import com.atguigu.hospital.model.HospitalSet;
import com.atguigu.hospital.model.Schedule;
import com.atguigu.hospital.service.ApiService;
import com.atguigu.hospital.util.BeanUtils;
import com.atguigu.hospital.util.HttpRequestHelper;
import com.atguigu.hospital.util.MD5;
import com.atguigu.hospital.util.YyghException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Autowired
    private ApiService apiService;

    @Value("classpath:hospital.json")
    private Resource hospitalResource;

    @Override
    public String getHoscode() {
        HospitalSet hospitalSet = hospitalSetMapper.selectById(1);
        return hospitalSet.getHoscode();
    }

    @Override
    public String getSignKey() {
        HospitalSet hospitalSet = hospitalSetMapper.selectById(1);
        return hospitalSet.getSignKey();
    }

    private String getApiUrl() {
        HospitalSet hospitalSet = hospitalSetMapper.selectById(1);
        return hospitalSet.getApiUrl();
    }

    @Override
    public JSONObject getHospital() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/hospital/show");
        System.out.println(respone.toJSONString());
        if(null != respone && 200 == respone.getIntValue("code")) {
            JSONObject jsonObject = respone.getJSONObject("data");
            return jsonObject;
        }
        return null;
    }

    @Override
    public boolean saveHospital(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("hosname",jsonObject.getString("hosname"));
        paramMap.put("hostype",jsonObject.getString("hostype"));
        paramMap.put("provinceCode",jsonObject.getString("provinceCode"));
        paramMap.put("cityCode", jsonObject.getString("cityCode"));
        paramMap.put("districtCode",jsonObject.getString("districtCode"));
        paramMap.put("address",jsonObject.getString("address"));
        paramMap.put("intro",jsonObject.getString("intro"));
        paramMap.put("route",jsonObject.getString("route"));
        //图片
        paramMap.put("logoData", jsonObject.getString("logoData"));

        JSONObject bookingRule = jsonObject.getJSONObject("bookingRule");
        paramMap.put("bookingRule",bookingRule.toJSONString());

        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap,this.getSignKey()));

        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveHospital");
        System.out.println(respone.toJSONString());

        if(null != respone && 200 == respone.getIntValue("code")) {
            return true;
        } else {
            throw new YyghException(respone.getString("message"), 201);
        }
    }

    @Override
    public Map<String, Object> findDepartment(int pageNum, int pageSize) {
        Map<String, Object> result = new HashMap();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        //paramMap.put("depcode",depcode);
        paramMap.put("page",pageNum);
        paramMap.put("limit",pageSize);
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/department/list");
        if(null != respone && 200 == respone.getIntValue("code")) {
            JSONObject jsonObject = respone.getJSONObject("data");

            result.put("total", jsonObject.getLong("totalElements"));
            result.put("pageNum", pageNum);
            result.put("list", jsonObject.getJSONArray("content"));
        } else {
            throw new YyghException(respone.getString("message"), 201);
        }
        return result;
    }

    @Override
    public boolean saveDepartment(String data) {
        JSONArray jsonArray = new JSONArray();
        if(!data.startsWith("[")) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            jsonArray.add(jsonObject);
        } else {
            jsonArray = JSONArray.parseArray(data);
        }

        for(int i=0, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hoscode",this.getHoscode());
            paramMap.put("depcode",jsonObject.getString("depcode"));
            paramMap.put("depname",jsonObject.getString("depname"));
            paramMap.put("intro",jsonObject.getString("intro"));
            paramMap.put("bigcode", jsonObject.getString("bigcode"));
            paramMap.put("bigname",jsonObject.getString("bigname"));

            paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
            paramMap.put("sign",HttpRequestHelper.getSign(paramMap, this.getSignKey()));
            JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveDepartment");
            System.out.println(respone.toJSONString());

            if(null == respone || 200 != respone.getIntValue("code")) {
                throw new YyghException(respone.getString("message"), 201);
            }
        }
        return true;
    }

    @Override
    public boolean removeDepartment(String depcode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("depcode",depcode);
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/department/remove");
        System.out.println(respone.toJSONString());
        if(null != respone && 200 == respone.getIntValue("code")) {
            return true;
        } else {
            throw new YyghException(respone.getString("message"), 201);
        }
    }

    @Override
    public Map<String, Object> findSchedule(int pageNum, int pageSize) {
        Map<String, Object> result = new HashMap();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        //paramMap.put("depcode",depcode);
        paramMap.put("page",pageNum);
        paramMap.put("limit",pageSize);
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/schedule/list");
        System.out.println(respone.toJSONString());
        if(null != respone && 200 == respone.getIntValue("code")) {
            JSONObject jsonObject = respone.getJSONObject("data");

            result.put("total", jsonObject.getLong("totalElements"));
            result.put("pageNum", pageNum);
            result.put("list", jsonObject.getJSONArray("content"));
        } else {
            throw new YyghException(respone.getString("message"), 201);
        }
        return result;
    }

    @Override
    public boolean saveSchedule(String data) {
        JSONArray jsonArray = new JSONArray();
        if(!data.startsWith("[")) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            jsonArray.add(jsonObject);
        } else {
            jsonArray = JSONArray.parseArray(data);
        }

        for(int i=0, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Long id = jsonObject.getLong("hosScheduleId");
            Schedule schedule = new Schedule();
            schedule.setId(id);
            schedule.setHoscode(this.getHoscode());
            schedule.setDepcode(jsonObject.getString("depcode"));
            schedule.setTitle(jsonObject.getString("title"));
            schedule.setDocname(jsonObject.getString("docname"));
            schedule.setSkill(jsonObject.getString("skill"));
            schedule.setWorkDate(jsonObject.getString("workDate"));
            schedule.setWorkTime(jsonObject.getInteger("workTime"));
            schedule.setReservedNumber(jsonObject.getInteger("reservedNumber"));
            schedule.setAvailableNumber(jsonObject.getInteger("availableNumber"));
            schedule.setAmount(jsonObject.getString("amount"));
            schedule.setStatus(1);

            Schedule targetSchedule = scheduleMapper.selectById(id);
            if(null != targetSchedule) {
                //
                BeanUtils.copyBean(schedule, targetSchedule, Schedule.class);
                scheduleMapper.updateById(targetSchedule);
            } else {
                scheduleMapper.insert(schedule);
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hoscode",schedule.getHoscode());
            paramMap.put("depcode",schedule.getDepcode());
            paramMap.put("title",schedule.getTitle());
            paramMap.put("docname",schedule.getDocname());
            paramMap.put("skill", schedule.getSkill());
            paramMap.put("workDate",schedule.getWorkDate());
            paramMap.put("workTime", schedule.getWorkTime());
            paramMap.put("reservedNumber",schedule.getReservedNumber());
            paramMap.put("availableNumber",schedule.getAvailableNumber());
            paramMap.put("amount",schedule.getAmount());
            paramMap.put("status",schedule.getStatus());
            paramMap.put("hosScheduleId",schedule.getId());
            paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
            paramMap.put("sign",HttpRequestHelper.getSign(paramMap, this.getSignKey()));

            JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveSchedule");
            System.out.println(respone.toJSONString());
            if(null == respone || 200 != respone.getIntValue("code")) {
                throw new YyghException(respone.getString("message"), 201);
            }
        }
        return false;
    }

    @Override
    public boolean removeSchedule(String hosScheduleId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("hosScheduleId",hosScheduleId);
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/schedule/remove");
        System.out.println(respone.toJSONString());
        if(null != respone && 200 == respone.getIntValue("code")) {
            return true;
        } else {
            throw new YyghException(respone.getString("message"), 201);
        }
    }

    @Override
    public void  saveBatchHospital() throws IOException {
        File file = hospitalResource.getFile();
        String jsonData = this.jsonRead(file);
        JSONArray jsonArray = JSONArray.parseArray(jsonData);
        for(int i=1, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hoscode","1000_"+i);
            paramMap.put("hosname",jsonObject.getString("hosname"));
            paramMap.put("hostype",i % 3 == 0 ? 1 : i % 3 == 1 ? 2 :4);
            paramMap.put("provinceCode","110000");
            paramMap.put("cityCode", "110100");
            if(i % 3 == 0) {
                paramMap.put("districtCode","110101");
            }
            if(i % 3 == 1) {
                paramMap.put("districtCode","110102");
            }
            if(i % 3 == 2) {
                paramMap.put("districtCode","110106");
            }

            paramMap.put("address","");
            String intro = "Beijing Union Medical College Hospital is a large tertiary Grade A comprehensive hospital that integrates medical treatment, teaching, and scientific research. It is designated by the National Health and Family Planning Commission as a national guidance center for the diagnosis and treatment of difficult and severe cases. It is also one of the earliest hospitals to undertake high-level healthcare and foreign medical tasks. With a complete range of disciplines, strong technical strength, outstanding specialty features, and strong interdisciplinary comprehensive advantages, it is renowned both domestically and internationally. In the China's Best Hospitals Ranking released by the Hospital Management Research Institute of Fudan University in 2010, 2011, 2012, 2013, and 2014, it ranked first for five consecutive years.\n" +
                    "\n" +
                    "The hospital was built in 1921 and founded by the Rockefeller Foundation. At the beginning of its establishment, the university was committed to building the best medical center in Asia. For over 90 years, the hospital has developed a spirit of rigor, excellence, diligence, and dedication and a characteristic cultural style of inclusiveness. It has established the modern medical education concept of three basics and three stricts, and has formed the three treasures of the hospital known as professors, medical records, and libraries. It has trained and nurtured a generation of medical masters such as Zhang Xiaoqian and Lin Qiaozhi, as well as several leading figures in modern Chinese medicine, and sent a large number of medical management talents to the country. It has also created more than 10 well-known large-scale comprehensive and specialized hospitals today. On the basis of summarizing 90 years of development experience, a new hospital concept was innovatively proposed in 2011, which is treating patients like family to improve patient satisfaction; treating colleagues like family to improve employee happiness.\n" +
                    "\n" +
                    "For over 90 years, the people of Peking Union Medical College have written a brilliant history with their persistent medical aspirations, noble medical ethics, exquisite medical skills, and rigorous academic style. Today, Peking Union Medical College people are continuing to work hard to build a internationally renowned and domestically first-class hospital.";
            paramMap.put("intro",intro);
            String route = "\n" +
                    "Bus routes in Dongyuan District: 106, 108, 110, 111, 116, 684, 685 to the north of Dongdan intersection; 41, 104 express, 814 bus to the south of Dongdan intersection; 1. 52. Road 802 to the west of Dongdan intersection; 20. Route 25, 37, 39 to the east of Dongdan intersection; Route 103, 104, 420, and 803 to Xindong'an Market; Subway lines 1 and 5 to Dongdan.\n" +
                    "Bus route in Xiyuan District: Route 68 to the east exit of Bicai Hutong; For more bus routes, please refer to the instructions.\n" +
                    "\n";
            paramMap.put("route",route);
            //logo
            //paramMap.put("logoData", this.getImgStr(jsonObject.getString("picture")));

            Map<String, Object> bookingRuleMap = new HashMap<>();
            bookingRuleMap.put("cycle",10);
            bookingRuleMap.put("releaseTime",jsonObject.getString("releaseTime"));
            bookingRuleMap.put("stopTime","12:30");
            bookingRuleMap.put("quitDay",-1);
            bookingRuleMap.put("quitTime","15:30");
            bookingRuleMap.put("rule","[\"West Campus Appointment Number Collection Location: West Campus Outpatient Building First Floor Hall Registration Window Collection\"]");
            paramMap.put("bookingRule",JSONObject.toJSONString(bookingRuleMap));

            paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
            paramMap.put("sign", HttpRequestHelper.getSign(paramMap, apiService.getSignKey()));

            JSONObject respone = HttpRequestHelper.sendRequest(paramMap,"http://localhost/api/hosp/saveHospital");
            System.out.println(respone.toJSONString());
            if(null == respone || 200 != respone.getIntValue("code")) {
                throw new YyghException(respone.getString("message"), 201);
            }
        }
    }

    private String jsonRead(File file){
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }

    public static String getImgStr(String imgFile){
        byte[] data = getImageBytes(imgFile);
        return new String(Base64.encodeBase64(data));
    }

    public static byte[] getImageBytes(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            //The length of the string read each time. If it's -1, it means all data has been read
            int len = 0;
            //Use an input stream to read data from the buffer
            while ((len = is.read(buffer)) != -1) {
                //Use an output stream to write data into the buffer.
                // The middle parameter represents the starting position to read from,
                // and 'len' represents the length to read
                outStream.write(buffer, 0, len);
            }
            // Encode the byte array into Base64
            return outStream.toByteArray();
        } catch (ConnectException e) {
            log.error("Connection Exception When Retrieving Image，url:{}",imgUrl,e);
        } catch (MalformedURLException e) {
            log.error("Exception Occurred with URL，url:{}",imgUrl,e);
        } catch (IOException e) {
            log.error("Exception Occurred While Reading Image，url:{}",imgUrl,e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return null;
    }
}
