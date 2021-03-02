package com.tal.wangxiao.conan.admin.constant;

/**
 * websocket常量
 * @author huyaoguo
 * @date 2020/1/13
 */
public class GlobalConst {

    /**
     * 前端监听board频道广播
     */
    public static final String TOPIC_BOARD = "/topic/board";

    public static final String ENDPOINT = "/ws";

    /**
     * 前端给后端发消息时地址前缀
     */
    public static final String APP_PREFIX = "/app";

    /**
     * 一对一给前端消息前缀
     */
    public static final String USER_PREFIX = "/user";


    /******************************/


    /**
     * 客户端响应消息
     */
    public static final String CLIENT_REPLY = "/reply";

    /**
     * 日志开始表示
     */
    public static final String LOG_START = "/log/start";
    /**
     * 日志结束表示
     */
    public static final String LOG_END = "/log/end";


    /********************************/

    public static final String LIVE_VIDEO_LOADING_SCENE = "com.xueersi.parentsmeeting.modules.livevideo.activity.LiveVideoLoadActivity";

    /****/

    public static final String KEY_MAPPING_LIST = "Android_Mapping_Version_List";

    public static final String KEY_PREFIX_MAPPING = "Android_Mapping_";



    /**board 推送频率**/
    public static final Integer SOCKET_FLUSH_TIME = 1000 * 15;
    public static final Integer SOCKET_SEND_DELAY = 300;

    /**ScheduledThreadPoolExecutor PoolSize 每个socket连接任务推送线程池**/
    public static final Integer SOCKET_THREAD_POOL_SIZE = 40;


}
