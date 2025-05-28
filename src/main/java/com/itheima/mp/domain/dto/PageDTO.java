package com.itheima.mp.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "分页查询结果")
public class PageDTO<T> {
    @ApiModelProperty("总条数")
    private Integer total;
    @ApiModelProperty("总页数")
    private  Integer pages;
    @ApiModelProperty("查询结果集")
    private List<T> list;//查询结果集不确定，泛型用？或者T
}
