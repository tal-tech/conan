package com.tal.wangxiao.conan.common.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.PageInfoResponse;
import com.tal.wangxiao.conan.sys.common.constant.HttpStatus;
import com.tal.wangxiao.conan.sys.common.core.page.PageDomain;
import com.tal.wangxiao.conan.sys.common.core.page.TableSupport;
import com.tal.wangxiao.conan.sys.common.utils.DateUtils;
import com.tal.wangxiao.conan.sys.common.utils.StringUtils;
import com.tal.wangxiao.conan.sys.common.utils.sql.SqlUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * @author: dengkunnan
 * @date: 2020-12-22 14:25
 * @description:
 **/
public class ConanBaseController {

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected ApiResponse getDataTable(List<?> list) {
        PageInfo pageInfo = new PageInfo(list);
        PageInfoResponse pageInfoResponse = PageInfoResponse.getPageResultByList(pageInfo);
        pageInfoResponse.setData(list);
        ApiResponse<PageInfoResponse> apiResponse = new ApiResponse<PageInfoResponse>();
        apiResponse.setCode(HttpStatus.SUCCESS);
        apiResponse.setMsg("查询成功");
        apiResponse.setData(pageInfoResponse);
        return apiResponse;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected ApiResponse toAjax(int rows) {
        return rows > 0 ? ApiResponse.success() : ApiResponse.error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }
}
