package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


	@Override
	@Cacheable(value = "dict",keyGenerator = "keyGenerator")  //add caching
	public List<Dict> findChildData(Long id) {
		QueryWrapper<Dict> wrapper = new QueryWrapper<>();
		wrapper.eq("parent_id",id);
		List<Dict> dictList = baseMapper.selectList(wrapper);
		for (Dict dict : dictList) {
			dict.setHasChildren(isChild(dict.getId()));
		}
		return dictList;
	}

	//

	@Override
	public void exportDictData(HttpServletResponse response) {
		//config download info
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		String fileName = "DataDict-"+ UUID.randomUUID();
		response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
		//query
		List<Dict> dictList = baseMapper.selectList(null);
		//Dict-->DictVo
		List<DictEeVo> dictEeVoList = new ArrayList<>();
		for (Dict dict : dictList) {
			DictEeVo dictEeVo = new DictEeVo();
			BeanUtils.copyProperties(dict,dictEeVo);
			dictEeVoList.add(dictEeVo);
		}
		//write
		try {
			EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
					 .doWrite(dictEeVoList);
		} catch (IOException e) {
			throw new YyghException("export error",500);
		}
	}

	//

	@Override
	@CacheEvict(value = "dict", allEntries=true)  //flush caching
	public void importDictData(MultipartFile multipartFile) {
		try {
			EasyExcel.read(multipartFile.getInputStream(),DictEeVo.class,new DictListener(baseMapper))
			         .sheet().doRead();
		} catch (IOException e) {
			throw new YyghException("import error",500);
		}
	}

	@Override
	public String getDictName(String dictCode, String value) {
		//
		if (StringUtils.isEmpty(dictCode)){
			QueryWrapper<Dict> wrapper = new QueryWrapper<>();
			wrapper.eq("value",value);
			Dict dict = baseMapper.selectOne(wrapper);
			if (dict!=null){
				return dict.getName();
			}else {
				return null;
			}
		}else {

			QueryWrapper<Dict> wrapper = new QueryWrapper<>();
			wrapper.eq("dict_code",dictCode);
			Dict dict = baseMapper.selectOne(wrapper);
			Long parent_id = dict.getId();
			return getDictByDictIdAndValue(value, parent_id);
		}
	}

	@Override
	public List<Dict> findByDictCode(String dictCode) {

		Dict dict = getDictByDictCode(dictCode);

		List<Dict> childData = findChildData(dict.getId());
		if (childData==null){
			return null;
		}
		return childData;
	}

	private Dict getDictByDictCode(String dictCode){
		QueryWrapper<Dict> wrapper = new QueryWrapper<>();
		wrapper.eq("dict_code",dictCode);
		return baseMapper.selectOne(wrapper);
	}
	private String getDictByDictIdAndValue(String value, Long id) {
		QueryWrapper<Dict> wrapper = new QueryWrapper<>();
		wrapper.eq("parent_id",id);
		wrapper.eq("value",value);
		Dict dict = baseMapper.selectOne(wrapper);
		return dict.getName();
	}


	private boolean isChild(Long id){
		QueryWrapper<Dict> wrapper = new QueryWrapper<>();
		wrapper.eq("parent_id",id);
		Integer count = baseMapper.selectCount(wrapper);
		return count>0;
	}

}
