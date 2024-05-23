package com.atguigu.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.yygh.oss.service.FileService;
import com.atguigu.yygh.oss.utils.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {



	@Override
	public String upload(MultipartFile file) {
		String endpoint = ConstantOssPropertiesUtils.EDNPOINT;
		String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
		String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;
		String bucketName = ConstantOssPropertiesUtils.BUCKET;


		OSS ossClient = null;

		try {
			ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
			InputStream inputStream = file.getInputStream();
			String fileName = new DateTime().toString("yyyy-MM-dd")+"/"+UUID.randomUUID().toString().replaceAll("-","")+""+file.getOriginalFilename();
			ossClient.putObject(bucketName, fileName, inputStream);

			String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
			return url;
		} catch (IOException e) {
			e.printStackTrace();
		}finally {

			ossClient.shutdown();
		}
		return null;
	}
}
