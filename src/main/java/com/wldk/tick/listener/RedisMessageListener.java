package com.wldk.tick.listener;

import com.wldk.tick.tuils.HttpUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author yll
 * @version 1.0
 * @date 2020/03/24
 */
@Component
public class RedisMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);


    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 消息体
        String body = new String(message.getBody());
        // 渠道名称
        String topic = new String(pattern);
        logger.info("推送定时任务数据{}",body);

        String[] split = body.split("-");
        HttpUtil.get(split[1],split[0]);

    }
}
