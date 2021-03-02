package com.tal.wangxiao.conan.admin.controller;

import com.tal.wangxiao.conan.admin.service.MessagesService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.entity.db.Messages;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.vo.MessagesVO;
import com.tal.wangxiao.conan.common.model.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 首页留言接口类
 *
 * @author huyaoguo
 * @date 2019/11/9
 */
@Api(value = "Messages API", tags = "留言板接口")
@RestController
@Slf4j
@RequestMapping("api/1.0/messages")
public class MessagesController {

    @Resource
    MessagesService messagesService;

    @ApiOperation(value = "用户留言", notes = "根据用户传来的留言信息留言")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "留言消息", dataType = "string", paramType = "query")
    })
    @PostMapping(value = "/add")
    public ApiResponse<Messages> addMessages(@RequestParam(value = "username") String username, @RequestParam(value = "content") String content) throws Exception {
        Result<Messages> result = messagesService.addOneMessage(username, content);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "留言列表", notes = "根据用户传来的留言信息留言")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "留言长度", dataType = "int", paramType = "query")
    })
    @GetMapping(value = "")
    public ApiResponse<PageVO<MessagesVO>> findMessagesList(@RequestParam(value = "size", required = false) Integer size) throws Exception {
        if (Objects.isNull(size)) {
            size = 100;
        }
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "id");
        Result<PageVO<MessagesVO>> result = messagesService.showMessagesBySize(pageable);
        return new ApiResponse<>(result);
    }

}
