package com.tal.wangxiao.conan.agent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 直播数据染色字段枚举类
 *
 * @author huyaoguo
 * @date 2020/3/17
 */
@AllArgsConstructor
@Getter
public enum DyteFiled {
    PLAN_ID("planid", "场次ID"),
    CLASS_ID("classid", "班级ID"),
    INTERACTION_ID("interactionid", "互动ID"),
    PACKAGE_ID("packageid", "包ID"),
    COURSE_ID("courseid", "课程ID"),
    TEACHER_ID("teacherid", "教师ID"),
    STUCOU_ID("stucouid", "学生课程ID"),
    TEAM_ID("teamid", "组Id"),
    STU_ID("stuid", "学生ID"),
    COURSEWARE_ID("coursewareid", "课件ID"),
    CLASS_IDS("classids", "班级列表Id");

    /**
     * 染色字段
     */
    private String filed;
    /**
     * 字段描述
     */
    private String desc;

}
