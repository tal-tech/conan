package com.tal.wangxiao.conan.common.constant;

/**
 * 任务调度常量
 *
 * @author liujinsong
 * @data 2019/10/15
 */
public class ScheduleConstants {
    public static final String TASK_CLASS_NAME = "__TASK_CLASS_NAME__";

    public static final String TASK_PROPERTIES = "__TASK_PROPERTIES__";

    public final static String JOB_JOBNAME_FOR_TASKSCHEDULING = "runAutomationScheduleTask";

    public final static String JOB_METHODNAME_FOR_TASKSCHEDULING = "runTask";

    /**
     * 默认
     */
    public static final String MISFIRE_DEFAULT = "默认";

    /** 立即触发执行 */
    //public static final String MISFIRE_IGNORE_MISFIRES = "0";

    /**
     * 触发一次执行
     */
    public static final String MISFIRE_ONCE_PROCEED = "执行一次";

    /**
     * 不触发立即执行
     */
    //public static final String MISFIRE_DO_NOTHING = "3";

    public enum Status {
        /**
         * 可执行
         */
        NORMAL(0, "运行"),

        /**
         * 禁用（暂停）
         */
        PAUSE(1, "暂停");


        private Integer value;

        private String label;

        private Status(Integer value,String label) {
            this.value = value;
            this.label = label;
        }

        public Integer getValue() {
            return value;
        }
        public String getLabel() {
            return label;
        }
    }
}
