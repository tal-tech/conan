//package com.tal.wangxiao.conan.common.domain;
//
//
//import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
//import io.swagger.annotations.ApiModelProperty;
//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;
//import io.swagger.annotations.ApiModel;
//
///**
// * work机管理对象 os_agent_config
// *
// * @author dengkunan
// * @date 2020-12-23
// */
//@ApiModel
//public class AgentConfig extends ConanBaseEntity
//{
//    private static final long serialVersionUID = 1L;
//
//    /** 主键 */
//    private Integer id;
//
//    /** kafka的groupId */
//
//    @ApiModelProperty("kafka的groupId")
//    private String name;
//
//    /** 机器状态，0-禁用，1-启用 */
//
//    @ApiModelProperty("机器状态，0-禁用，1-启用")
//    private Integer isEnable;
//
//    /** 运行状态，0-准备，1-运行中，2- */
//
//    @ApiModelProperty("运行状态，0-准备，1-运行中，2-")
//    private Integer isBusy;
//
//    public void setId(Integer id)
//    {
//        this.id = id;
//    }
//
//    public Integer getId()
//    {
//        return id;
//    }
//    public void setName(String name)
//    {
//        this.name = name;
//    }
//
//    public String getName()
//    {
//        return name;
//    }
//    public void setIsEnable(Integer isEnable)
//    {
//        this.isEnable = isEnable;
//    }
//
//    public Integer getIsEnable()
//    {
//        return isEnable;
//    }
//    public void setIsBusy(Integer isBusy)
//    {
//        this.isBusy = isBusy;
//    }
//
//    public Integer getIsBusy()
//    {
//        return isBusy;
//    }
//
//    @Override
//    public String toString() {
//        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
//            .append("id", getId())
//            .append("name", getName())
//            .append("isEnable", getIsEnable())
//            .append("isBusy", getIsBusy())
//            .toString();
//    }
//}