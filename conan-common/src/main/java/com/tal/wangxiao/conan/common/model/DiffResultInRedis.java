package com.tal.wangxiao.conan.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DiffResultInRedis implements Serializable {

	private int allKeysNu;
	
	private int sameKeyNu;
	
	private String diffMsg;
	
	private boolean isDiffResult;
	
	
}
