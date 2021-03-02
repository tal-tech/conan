package com.tal.wangxiao.conan.utils.schema;

import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.Data;


/**
 * @author ：dengkunnan
 * @date ：2020.05.25
 * @description：对JSONSchema返回信息进行封装
 * @modified By：
 * @version: 1.0.0.1
 */
@Data
public class JSONSchemaResult {

    /**检查是否成功*/
    private  Boolean isSuccess;

    /**检查发现错误描述信息*/
    private Object desc;

    @Override
    public String toString(){
        if(StringHandlerUtils.isNull(desc)) {
            return  "isSuccess =" + isSuccess;
        }
        return desc.toString();
    }

}
