package com.wldk.tick.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MessageSet {
    /**
     * key值
     */
    @NotNull(message = "key不能为空")
    String Key;

    /**
     * 通知时间（秒单位）
     */
    @NotNull(message = "通知时间不能为空")
    Long TimeOut;

}
