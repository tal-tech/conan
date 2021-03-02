package com.tal.wangxiao.conan.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 回放记录（不包括各接口回放详情）
 * @author huyaoguo
 * @date 2021/1/25
 */
@Data
@ApiModel(value = "ReplayVO", description = "流量录制信息")
public class ReplayVO {
    @JsonProperty(index = 1)
    private Integer id;

    @JsonProperty(value = "operate_by", index = 2)
    private String operateBy;

    @JsonProperty(value = "start_at", index = 13)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;

    @JsonProperty(value = "end_at", index = 4)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endAt;

    @JsonProperty(value = "success_rate", index = 5)
    private Double successRate;

    @JsonProperty(value = "is_baseline", index = 6)
    private Boolean isBaseline;

    @JsonProperty(value = "replay_env", index = 6)
    private String replayEnv;

    @JsonProperty(value = "replay_type", index = 6)
    private Integer replayType;
}
