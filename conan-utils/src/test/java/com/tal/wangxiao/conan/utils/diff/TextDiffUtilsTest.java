package com.tal.wangxiao.conan.utils.diff;

import com.tal.wangxiao.conan.utils.diff.core.TextDiffResult;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: dengkunnan
 * @date: 2021-01-15 14:55
 * @description:
 **/
public class TextDiffUtilsTest {


    @Test
    public void stringDiff() {
        TextDiffUtils textUtils = new TextDiffUtils();
        Set<String> ignore = new HashSet<String>();
        ignore.add("xxx");
        try {
            TextDiffResult textDiffResult = textUtils.stringDiff("ss", "ssm", ignore);

        } catch (Exception e) {
            System.out.println("run fail");
        }

    }
}
