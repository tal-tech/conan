package com.tal.wangxiao.conan.common.model;

import lombok.Data;

@Data
public class DiffResultInRedis {

	private int allKeysNu;
	
	private int sameKeyNu;
	
	private String diffMsg;
	
	private boolean isDiffResult;
	
	
}
