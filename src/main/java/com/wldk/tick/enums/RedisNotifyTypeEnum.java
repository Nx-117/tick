package com.wldk.tick.enums;

/**
 * @author yll
 * @date 2019/4/29 23:51
 */
public enum RedisNotifyTypeEnum {
    /**
     * 键过期事件
     */
    EXPIRE("expire",""),
    /**
     * 键注册事件
     */
    REGIST("regist","");

    String value;
    /**
     * 事件描述
     */
    String desc;

    RedisNotifyTypeEnum(String value,String desc) {
        this.value = value;
        this.desc = desc;
    }}
