package com.wldk.tick.domain;

import lombok.Data;

@Data
public class ReturnData {
    /**
     * 状态码
     */
    Integer Code;

    /**
     * 返回信息
     */
    String Message;

    /**
     * 返回数据
     */

    Object Data;

}
