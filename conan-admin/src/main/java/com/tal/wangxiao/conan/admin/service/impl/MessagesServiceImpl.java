package com.tal.wangxiao.conan.admin.service.impl;

import com.tal.wangxiao.conan.admin.service.MessagesService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.converter.ConvertUtil;
import com.tal.wangxiao.conan.common.converter.MessagesConverter;
import com.tal.wangxiao.conan.common.entity.db.Messages;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.vo.MessagesVO;
import com.tal.wangxiao.conan.common.model.vo.PageVO;
import com.tal.wangxiao.conan.common.repository.db.MessagesRepository;
import com.tal.wangxiao.conan.sys.auth.core.domain.model.LoginUser;
import com.tal.wangxiao.conan.sys.common.utils.ServletUtils;
import com.tal.wangxiao.conan.sys.framework.web.service.TokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class MessagesServiceImpl implements MessagesService {

    @Resource
    MessagesRepository messagesRepository;

    @Resource
    TokenService tokenService;


    @Override
    public Result<Messages> addOneMessage(String username, String content) {
        Messages oneMessages = new Messages();
        oneMessages.setContent(content);
        oneMessages.setCreateAt(LocalDateTime.now());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        oneMessages.setCreateBy(loginUser.getUser().getUserId().intValue());
        Messages messages = messagesRepository.save(oneMessages);
        return new Result<>(ResponseCode.SUCCESS, messages);
    }

    //根据传入size展示消息显示大小
    @Override
    public Result<PageVO<MessagesVO>> showMessagesBySize(Pageable pageable) {
        Page<Messages> messagesPage = messagesRepository.findAll(pageable);
        PageVO<MessagesVO> messagesVOPageVO = ConvertUtil.convert2PageVO(messagesPage, MessagesVO.class, new MessagesConverter());
        return new Result<>(ResponseCode.SUCCESS, messagesVOPageVO);
    }
}
