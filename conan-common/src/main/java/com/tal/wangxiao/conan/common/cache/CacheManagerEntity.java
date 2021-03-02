package com.tal.wangxiao.conan.common.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存实体类
 *
 * @author huyaoguo
 * @date 2020/12/15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheManagerEntity {

    /**
     * 保存的数据
     */
    private Object data;

    /**
     * 设置缓存数据失效时间，为0表示永不失效
     */
    private long timeOut;

    /**
     * 最后刷新时间
     */
    private long lastRefreshTime;

}
