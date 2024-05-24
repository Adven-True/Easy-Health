package com.atguigu.yygh.model.hosp;

import com.atguigu.yygh.model.base.BaseEntity;
import com.atguigu.yygh.model.base.BaseMongoEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@ApiModel(description = "Department")
@Document("Department")
public class Department extends BaseMongoEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "hospital code")
	@Indexed //normal index
	private String hoscode;

	@ApiModelProperty(value = "department code")
	@Indexed(unique = true) //unique Index
	private String depcode;

	@ApiModelProperty(value = "department name")
	private String depname;

	@ApiModelProperty(value = "department description")
	private String intro;

	@ApiModelProperty(value = "big department code")
	private String bigcode;

	@ApiModelProperty(value = "big department name")
	private String bigname;

}

