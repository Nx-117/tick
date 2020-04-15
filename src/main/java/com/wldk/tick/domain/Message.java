package com.wldk.tick.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Message {
    @NotNull(message = "key值不能为空")
    List<MessageSet> SetList;

    /**
     * 通知地址 get方式 例：http://www.baidu.com/abc？key=（到期的key）
     */
    @NotNull(message = "回调地址不能为空")
    String NotifyUrl;

    /**
     * 是否删除 1是0否  默认未0  不传值为0
     */
    int isDel;
}
