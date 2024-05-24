package com.atguigu.yygh.cmn.service;


import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface DictService extends IService<Dict> {

	/**
	 *
	 * @param id
	 * @return
	 */
	List<Dict> findChildData(Long id);

	/**
	 *
	 * @param response
	 */
	void exportDictData(HttpServletResponse response);

	/**
	 *
	 * @param multipartFile
	 */
	void importDictData(MultipartFile multipartFile);

	/**
	 *
	 * @param dictCode
	 * @param value
	 * @return
	 */
	String getDictName(String dictCode, String value);

	/**
	 *
	 * @param dictCode
	 * @return
	 */
	List<Dict> findByDictCode(String dictCode);
}
