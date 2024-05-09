package com.atguigu.yygh.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseMongoEntity implements Serializable {

    @ApiModelProperty(value = "id")
    @Id
    private String id;

    @ApiModelProperty(value = "Create Time")
    private Date createTime;

    @ApiModelProperty(value = "Update Time")
    private Date updateTime;

    @ApiModelProperty(value = "Logical Deletion (1: Deleted, 0: Not Deleted)")
    private Integer isDeleted;

    @ApiModelProperty(value = "Other parameters")
    @Transient //Properties annotated with this annotation will not be persisted in the database.
    // They will only serve as regular JavaBean properties
    private Map<String,Object> param = new HashMap<>();
}
