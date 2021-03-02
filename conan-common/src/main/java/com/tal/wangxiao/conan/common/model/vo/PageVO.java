package com.tal.wangxiao.conan.common.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页信息VO
 * @author huyaoguo
 * @date 2021/1/25
 */
@Data
public class PageVO<E> {
    /**
     * 页码
     */
    @JsonProperty(value = "page")
    private Integer pageNum;
    /**
     * 页大小
     */
    @JsonProperty(value = "size")
    private Integer pageSize;
    /**
     * 总行数
     */
    private Long total;
    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 记录列表
     */
    private List<E> data;
}
