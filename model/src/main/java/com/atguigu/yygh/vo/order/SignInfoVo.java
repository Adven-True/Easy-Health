package com.atguigu.yygh.vo.order;

import com.atguigu.yygh.model.base.BaseMongoEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Data
@ApiModel(description = "signature information")
public class SignInfoVo  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "api base url")
	private String apiUrl;

	@ApiModelProperty(value = "signature key")
	private String signKey;

}

