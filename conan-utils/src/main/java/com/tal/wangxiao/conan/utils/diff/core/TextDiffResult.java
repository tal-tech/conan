package com.tal.wangxiao.conan.utils.diff.core;

import java.util.List;

import lombok.Data;

/**
 * 文本对比返回对象
 * @author liujinsong
 * @date 2019/6/26
 */
@Data
public class TextDiffResult {

	/**
	 * 错误描述
	 * */
	private List<String> diffMsgList;

	/**
	 * 是否相等
	 * */
	private boolean isEqual;



	/**
	 * Base Txt length
	 * */
	private int totalMsgCount;

	/**
	 * 不一致 长度
	 * */
	private int diffMsgCount;
	
	
}
