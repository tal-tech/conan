package com.tal.wangxiao.conan.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * VO对象基类
 *
 * @author conan
 */
@Data
public abstract class BaseVO {

    @JsonProperty(value = "create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    @JsonProperty(value = "create_by")
    private String createBy;

    @JsonProperty(value = "update_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    @JsonProperty(value = "update_by")
    private String updateBy;
}
