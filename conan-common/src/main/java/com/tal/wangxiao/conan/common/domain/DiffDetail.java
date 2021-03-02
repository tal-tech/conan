package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 比对接口关系表对象 bss_diff_detail
 *
 * @author dengkunan
 * @date 2021-01-08
 */
@ApiModel
public class DiffDetail extends ConanBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer diffDetailId;

    /**
     * $column.columnComment
     */

    @ApiModelProperty("${comment}")
    private Integer actualCount;

    /**
     * $column.columnComment
     */

    @ApiModelProperty("${comment}")
    private Integer apiId;

    /**
     * $column.columnComment
     */

    @ApiModelProperty("${comment}")
    private Integer diffId;

    /**
     * $column.columnComment
     */

    @ApiModelProperty("${comment}")
    private Integer expectCount;

    public void setActualCount(Integer actualCount) {
        this.actualCount = actualCount;
    }

    public Integer getActualCount() {
        return actualCount;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setDiffId(Integer diffId) {
        this.diffId = diffId;
    }

    public Integer getDiffId() {
        return diffId;
    }

    public void setExpectCount(Integer expectCount) {
        this.expectCount = expectCount;
    }

    public Integer getExpectCount() {
        return expectCount;
    }

    public Integer getDiffDetailId() {
        return diffDetailId;
    }

    public void setDiffDetailId(Integer diffDetailId) {
        this.diffDetailId = diffDetailId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("diffDetailId" , getDiffDetailId())
                .append("actualCount" , getActualCount())
                .append("apiId" , getApiId())
                .append("diffId" , getDiffId())
                .append("expectCount" , getExpectCount())
                .toString();
    }
}