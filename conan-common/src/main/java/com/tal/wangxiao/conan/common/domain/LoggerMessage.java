package com.tal.wangxiao.conan.common.domain;

import lombok.Data;

@Data
public class LoggerMessage {
    private String body; //消息内容
    private String timestamp; //时间

    public LoggerMessage(String body, String timestamp) {
        this.body = body;
        this.timestamp = timestamp;
    }

    public LoggerMessage() {
    }
}