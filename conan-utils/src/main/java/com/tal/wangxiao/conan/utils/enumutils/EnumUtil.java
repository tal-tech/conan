package com.tal.wangxiao.conan.utils.enumutils;

import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举工具类
 *
 * @author dengkunnan
 */
@Slf4j
public class EnumUtil {

    public static <T extends Enum<T>> T getByField(Class<T> clazz, String getTypeCodeMethodName, String value) {
        if (StringHandlerUtils.isNull(value)) {
            return null;
        }
        T result = null;
        try {
            //Enum接口中没有values()方法，我们仍然可以通过Class对象取得所有的enum实例
            T[] arr = clazz.getEnumConstants();
            //获取定义的方法
            Method targetMethod = clazz.getDeclaredMethod(getTypeCodeMethodName);
            String typeCodeVal;
            //遍历枚举定义
            for (T entity : arr) {
                //获取传过来方法的
                typeCodeVal = targetMethod.invoke(entity).toString();
                //执行的方法的值等于参数传过来要判断的值
                if (value.equals(typeCodeVal)) {
                    result = entity;
                    break;
                }
            }
        } catch (IllegalAccessException | SecurityException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
            log.info("得到enum实例失败，" + e);
        }
        return result;
    }

}
