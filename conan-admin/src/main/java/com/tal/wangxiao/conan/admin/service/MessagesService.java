package com.tal.wangxiao.conan.admin.service;

import com.tal.wangxiao.conan.common.entity.db.Messages;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.vo.MessagesVO;
import com.tal.wangxiao.conan.common.model.vo.PageVO;
import org.springframework.data.domain.Pageable;

public interface MessagesService {

    Result<Messages> addOneMessage(String username, String content);

    Result<PageVO<MessagesVO>> showMessagesBySize(Pageable pageable);
}
