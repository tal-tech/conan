package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 测试账号管理对象 bss_test_user
 * 
 * @author dengkunan
 * @date 2020-12-30
 */
@ApiModel
public class TestUser extends ConanBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 名称 */
   
    @ApiModelProperty("名称")
    private String username;

    /** 密码 */
   
    @ApiModelProperty("密码")
    private String password;

    /** 类型 0/1 学生/老师 */
   
    @ApiModelProperty("类型 0/1 学生/老师")
    private Integer type;

    /** 用户ID */
   
    @ApiModelProperty("用户ID")
    private String userid;

    /** 用户状态 0/1 正常/锁定 */
   
    @ApiModelProperty("用户状态 0/1 正常/锁定")
    private Integer status;

    /** 部门id */
   
    @ApiModelProperty("部门id")
    private Integer sysDeptId;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setUserid(String userid) 
    {
        this.userid = userid;
    }

    public String getUserid() 
    {
        return userid;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setSysDeptId(Integer sysDeptId) 
    {
        this.sysDeptId = sysDeptId;
    }

    public Integer getSysDeptId() 
    {
        return sysDeptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("type", getType())
            .append("userid", getUserid())
            .append("status", getStatus())
            .append("sysDeptId", getSysDeptId())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}