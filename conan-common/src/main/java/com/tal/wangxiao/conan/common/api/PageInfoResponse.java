package com.tal.wangxiao.conan.common.api;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ：dengkunnan
 * @date ：2020.06.1
 * @description：分页Result消息处理工具
 * @modified By：
 * @version: 1.0.0.1
 */
@Data
public class PageInfoResponse<T> {

    @ApiModelProperty("总计")
    private long total = 0;
    @ApiModelProperty("总页数")
    private  long pages = 0;
    @ApiModelProperty("当前页")
    private  long page = 0;
    @ApiModelProperty("当前页长度")
    private  long size = 0;
    @ApiModelProperty("结果数据")
    private T data = null;


    public  static PageInfoResponse getPageResultByList(PageInfo pageInfo) {
        PageInfoResponse pageInfoHandlerUtils = new PageInfoResponse<>();
        pageInfoHandlerUtils.setTotal(pageInfo.getTotal());
        pageInfoHandlerUtils.setPages(pageInfo.getPages());
        pageInfoHandlerUtils.setPage(pageInfo.getPageNum());
        pageInfoHandlerUtils.setSize(pageInfo.getSize());
        pageInfoHandlerUtils.setData(pageInfo.getList());
        return pageInfoHandlerUtils;
    }



}
