package com.tal.wangxiao.conan.common.model.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页基本数据
 *
 * @author huyaoguo
 * @date 2021/1/14
 **/
@Data
public class HomeBasicData {

    @ApiModelProperty(value = "接入接口总数", name = "api_count")
    @JsonProperty(value = "api_count")
    String apiCount;

    @ApiModelProperty(value = "累计回放总数", name = "replay_count")
    @JsonProperty(value = "replay_count")
    String replayCount;

    @ApiModelProperty(value = "累计回放流量", name = "replay_flows")
    @JsonProperty(value = "replay_flows")
    String replayFlows;

    @ApiModelProperty(value = "比对执行次数", name = "diff_count")
    @JsonProperty(value = "diff_count")
    String diffCount;

    @ApiModelProperty(value = "平台活跃用户数", name = "user_count")
    @JsonProperty(value = "user_count")
    String userCount;

    @ApiModelProperty(value = "平台累计访问量(pv)", name = "pv_count")
    @JsonProperty(value = "pv_count")
    String pvCount;
}
