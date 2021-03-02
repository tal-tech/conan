package com.tal.wangxiao.conan.agent.constant;

/**
 * @author ：dengkunnan
 * @date ：2020.06.03
 * @description： 针对JSON可配置数据类型枚举
 * @version: 1.0.0.1
 */
public enum DataType {

    string, integer, number, object, array, Boolean;

    public static DataType getDataType(String type) {
        return valueOf(type);
    }

}
