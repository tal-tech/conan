package com.tal.wangxiao.conan.common.converter;

import com.tal.wangxiao.conan.common.entity.db.Messages;
import com.tal.wangxiao.conan.common.entity.db.User;
import com.tal.wangxiao.conan.common.model.vo.MessagesVO;
import com.tal.wangxiao.conan.common.repository.db.UserRepository;
import com.tal.wangxiao.conan.common.utils.spring.SpringBeanUtil;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author huyaoguo
 * @date 2020/11/12
 */
public class MessagesConverter extends AbstractObjectConverter<Messages, MessagesVO> {

    @Resource
    UserRepository userRepository;

    public MessagesConverter(){
        userRepository = SpringBeanUtil.getBean(UserRepository.class);
    }

    @Override
    public void extraHandle(Messages messages, MessagesVO messagesVO) {
        //时间戳格式转换
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String createAt = dtf2.format(messages.getCreateAt());
        String userName = "匿名";
        Optional<User> userOptional = userRepository.findById(messages.getCreateBy());
        if(userOptional.isPresent()){
            userName = userOptional.get().getNickName();
        }
        messagesVO.setTimestamp(createAt);
        messagesVO.setComment(userName+" 提交于 "+createAt);
    }
}
