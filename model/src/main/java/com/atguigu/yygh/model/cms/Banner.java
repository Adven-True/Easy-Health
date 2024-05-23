package com.atguigu.yygh.model.cms;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "homepage Banner entity")
@TableName("banner")
public class Banner extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "title")
	@TableField("title")
	private String title;

	@ApiModelProperty(value = "image url")
	@TableField("image_url")
	private String imageUrl;

	@ApiModelProperty(value = "link url")
	@TableField("link_url")
	private String linkUrl;

	@ApiModelProperty(value = "order")
	@TableField("sort")
	private Integer sort;

}

