package com.tal.wangxiao.conan.utils.random;

import java.util.Random;

/**
 * 数字随机数
 *
 * @author huyaoguo
 * @date 2021/1/6
 **/
public class RandomUtils {

    /**
     * 得到2个数中间一个数的随机数
     * @param min 开始数
     * @param max 结尾数
     * @return 随机数
     */
    public static int getRandom(int min, int max) {
        if(min>=max){
            return min;
        }
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

}
