package com.tal.wangxiao.conan.common.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author huyaoguo
 * @date 2020/11/12
 * @description 留言VO
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MessagesVO", description = "从前端接收更新接口打标相关的数据")
public class MessagesVO implements Serializable {

    @JsonProperty(value = "content")
    @ApiModelProperty(value = "留言内容", name = "content")
    String content;
    @ApiModelProperty(value = "时间戳", name = "timestamp")
    String timestamp;
    @ApiModelProperty(value = "底部标注", name = "comment")
    String comment;

}
