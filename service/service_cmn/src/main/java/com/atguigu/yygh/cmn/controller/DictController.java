package com.atguigu.yygh.cmn.controller;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "dict interface")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

	@Autowired
	private DictService dictService;



	/**
	 * import data
	 * @param multipartFile Excel file
	 * @return response
	 */
	@PostMapping("importData")
	@CacheEvict(value = "dict", allEntries=true)//update cache
	public Result importDict(MultipartFile multipartFile){
		if (multipartFile!=null){
			dictService.importDictData(multipartFile);
			return Result.ok();
		}
		System.out.println("multipartFile is null");
		return Result.fail();
	}

	/**
	 *
	 * @param response
	 * @return
	 */
	@GetMapping("exportData")
	public void exportDict(HttpServletResponse response){
		dictService.exportDictData(response);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("findChildData/{id}")
//	@Cacheable(value = "dict",keyGenerator = "keyGenerator")
	public Result findChildData(@PathVariable("id") Long id){
		List<Dict> list = dictService.findChildData(id);
		return Result.ok(list);
	}

	@GetMapping("getName/{dictCode}/{value}")
	public String getName(@PathVariable("dictCode") String dictCode,
						  @PathVariable("value") String value){
		String dictName = dictService.getDictName(dictCode,value);
		return dictName;
	}

	@GetMapping("getName/{value}")
	public String getName(@PathVariable("value") String value){
		String dictName = dictService.getDictName("",value);
		return dictName;
	}


	@ApiOperation(value="根据dictCode获取下级节点")
	@GetMapping("findByDictCode/{dictCode}")
	public Result<List<Dict>> findByDictCode(@PathVariable("dictCode") String dictCode){
		List<Dict> list = dictService.findByDictCode(dictCode);
		return Result.ok(list);
	}
}
