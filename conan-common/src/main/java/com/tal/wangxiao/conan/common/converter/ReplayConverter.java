package com.tal.wangxiao.conan.common.converter;


import com.tal.wangxiao.conan.common.entity.db.Replay;
import com.tal.wangxiao.conan.common.entity.db.User;
import com.tal.wangxiao.conan.common.model.vo.ReplayVO;
import com.tal.wangxiao.conan.common.repository.db.UserRepository;
import com.tal.wangxiao.conan.common.utils.spring.SpringBeanUtil;

import java.util.Optional;

/**
 * 流量回放记录DO转VO工具类
 * @author huyaoguo
 * @date 2021/1/25
 */
public class ReplayConverter extends AbstractObjectConverter<Replay, ReplayVO> {
    private UserRepository userRepository;

    public ReplayConverter() {
        userRepository = SpringBeanUtil.getBean(UserRepository.class);
    }

    @Override
    public void extraHandle(Replay replay, ReplayVO replayVO) {
        //转换为创建人姓名
        Optional<User> operateByOptional = userRepository.findById(replay.getOperateBy());
        operateByOptional.ifPresent(user -> replayVO.setOperateBy(user.getUserName()));

    }
}
