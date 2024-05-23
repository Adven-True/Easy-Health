package com.atguigu.yygh.model.cmn;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Data
@ApiModel(description = "data dictionary")
@TableName("dict")
public class Dict {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "Create Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "Update Time")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "Logical Deletion (1: Deleted, 0: Not Deleted)")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    @ApiModelProperty(value = "Other parameters")
    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();

    @ApiModelProperty(value = "superior id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "name")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "value")
    @TableField("value")
    private String value;

    @ApiModelProperty(value = "dict code")
    @TableField("dict_code")
    private String dictCode;

    @ApiModelProperty(value = "contained children or not")
    @TableField(exist = false)
    private boolean hasChildren;

}