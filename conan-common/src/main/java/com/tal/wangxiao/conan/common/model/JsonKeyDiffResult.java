package com.tal.wangxiao.conan.common.model;

import lombok.Data;

import java.util.List;

/**
 * json结构比较结果实体
 *
 */
@Data
public class JsonKeyDiffResult {

    private List<String> diffMsgList;

    private boolean isEqual;

    private int totalMsgCount;

    private int diffMsgCount;

}
