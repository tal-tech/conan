package com.tal.wangxiao.conan.common.api;

import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.sys.common.constant.HttpStatus;
import com.tal.wangxiao.conan.sys.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 统一服务端接口返回错误信息格式
 *
 * @author huyaoguo
 * @date 2020/12/02
 */
@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "通用API接口返回", description = "Common Api Response")
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = -8987146499044811408L;
    /**
     * 通用返回状态
     */
    @ApiModelProperty(value = "通用返回状态", required = true)
    private Integer code;
    /**
     * 通用返回信息
     */
    @ApiModelProperty(value = "通用返回信息", required = true)
    private String msg;
    /**
     * 通用返回数据
     */
    @ApiModelProperty(value = "通用返回数据", required = true)
    private T data;

    /**
     * 初始化一个新创建的 ApiResponse对象
     * @param result Result
     */
    public ApiResponse(Result<T> result) {
        this.code = result.getRespCode().getCode();
        this.msg = result.getRespCode().getDesc();
        data = result.getDesc();
    }

    /**
     * 初始化一个新创建的 ApiResponse 对象，使其表示一个空消息。
     */
    public ApiResponse() {
    }

    /**
     * 初始化一个新创建的 ApiResponse 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public ApiResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 初始化一个新创建的 ApiResponse 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public ApiResponse(int code, String msg, T data) {

        this.code = code;
        this.msg = msg;
        if (StringUtils.isNotNull(data)) {
            this.data = data;
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ApiResponse success() {
        return ApiResponse.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static ApiResponse success(Object data) {
        return ApiResponse.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ApiResponse success(String msg) {
        return ApiResponse.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ApiResponse success(String msg, Object data) {
        return new ApiResponse(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static ApiResponse error() {
        return ApiResponse.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ApiResponse error(String msg) {
        return ApiResponse.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ApiResponse error(String msg, Object data) {
        return new ApiResponse(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static ApiResponse error(int code, String msg) {
        return new ApiResponse(code, msg, null);
    }


}
