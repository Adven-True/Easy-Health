package com.atguigu.yygh.model.acl;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Data
@ApiModel(description = "Permission")
@TableName("acl_permission")
public class Permission extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "superior")
	@TableField("pid")
	private Long pid;

	@ApiModelProperty(value = "name")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "type(1:menu,2:button)")
	@TableField("type")
	private Integer type;

	@ApiModelProperty(value = "permission value")
	@TableField("permission_value")
	private String permissionValue;

	@ApiModelProperty(value = "path")
	@TableField("path")
	private String path;

	@ApiModelProperty(value = "component")
	@TableField("component")
	private String component;

	@ApiModelProperty(value = "icon")
	@TableField("icon")
	private String icon;

	@ApiModelProperty(value = "status(0:Disabled,1:Working)")
	@TableField("status")
	private Integer status;

	@ApiModelProperty(value = "level")
	@TableField(exist = false)
	private Integer level;

	@ApiModelProperty(value = "subordinate")
	@TableField(exist = false)
	private List<Permission> children;

	@ApiModelProperty(value = "selected or not")
	@TableField(exist = false)
	private boolean isSelect;

}

