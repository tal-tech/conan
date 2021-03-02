package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * ES 数据源配置，域名需要绑定ES数据源对象 bss_es_source
 *
 * @author dengkunan
 * @date 2021-01-05
 */
@ApiModel
public class EsSource extends ConanBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer esSourceId;

    /**
     * ES名称
     */

    @ApiModelProperty("ES名称")
    private String esName;

    /**
     * ES Bean名称(全英文)
     */

    @ApiModelProperty("ES Bean名称(全英文)")
    private String esBeanName;

    /**
     * $column.columnComment
     */

    @ApiModelProperty("ES Bean名称(全英文)")
    private String esIp;

    /**
     * $column.columnComment
     */

    @ApiModelProperty("ES Bean名称(全英文)")
    private Integer esPort;

    public void setEsSourceId(Integer esSourceId) {
        this.esSourceId = esSourceId;
    }

    public Integer getEsSourceId() {
        return esSourceId;
    }

    public void setEsName(String esName) {
        this.esName = esName;
    }

    public String getEsName() {
        return esName;
    }

    public void setEsBeanName(String esBeanName) {
        this.esBeanName = esBeanName;
    }

    public String getEsBeanName() {
        return esBeanName;
    }

    public void setEsIp(String esIp) {
        this.esIp = esIp;
    }

    public String getEsIp() {
        return esIp;
    }

    public void setEsPort(Integer esPort) {
        this.esPort = esPort;
    }

    public Integer getEsPort() {
        return esPort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("esSourceId", getEsSourceId())
                .append("esName", getEsName())
                .append("esBeanName", getEsBeanName())
                .append("esIp", getEsIp())
                .append("esPort", getEsPort())
                .toString();
    }
}