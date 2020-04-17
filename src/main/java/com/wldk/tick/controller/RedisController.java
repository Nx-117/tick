package com.wldk.tick.controller;

import com.wldk.tick.domain.Message;
import com.wldk.tick.domain.MessageSet;
import com.wldk.tick.domain.ReturnData;
import com.wldk.tick.listener.RedisMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yll
 * @version 1.0
 * @date 2020/03/24
 */
@RestController
public class RedisController {
    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    private RedisTemplate redisTemplate;

    private StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "expire";
    //成功状态码
    private static final Integer SUCCESSS_CODE = 200;
    //失败状态码
    private static final Integer FAIL_CODE = 999;

    @Autowired
    public RedisController(RedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 订阅服务
     *
     * @param message 用户信息
     * @return 成功标识
     */
    @RequestMapping(path = "/tick", method = RequestMethod.POST)
    public Object insert(@RequestBody Message message) {
        ReturnData data = new ReturnData();
        // 非空判断
        if (null == message) {
            data.setCode(FAIL_CODE);
            data.setMessage("未获取到参数");
            return data;
        }
        if (message.getNotifyUrl() == null || "".equals(message.getNotifyUrl())) {
            data.setCode(FAIL_CODE);
            data.setMessage("回调地址不能为空");
            return data;
        }
        List<MessageSet> setList = message.getSetList();
        if (setList == null) {
            data.setCode(FAIL_CODE);
            data.setMessage("未获取到key值");
            return data;
        }

        if (message.getIsDel() != 0 && message.getIsDel() != 1) {
            data.setCode(FAIL_CODE);
            data.setMessage("isDel参数错误");
            return data;
        }

        Map<String, Boolean> map = new HashMap<>();
        if (message.getIsDel() == 0) {
            logger.info("添加/更新定时任务{}", message);
            for (MessageSet set : message.getSetList()) {
                StringBuilder key = new StringBuilder();
                key.append(set.getKey()).append("-").append(message.getNotifyUrl());
                String redisKey = key.toString();
                // 存储过期键
                stringRedisTemplate.opsForValue().set(redisKey, "");
                // 设置过期时间
                map.put(set.getKey(), stringRedisTemplate.expire(redisKey, set.getTimeOut(), TimeUnit.SECONDS));
            }
        } else {
            logger.info("删除定时任务{}", message);
            for (MessageSet set : message.getSetList()) {
                StringBuilder key = new StringBuilder();
                key.append(set.getKey()).append("-").append(message.getNotifyUrl());
                String redisKey = key.toString();
                map.put(set.getKey(), stringRedisTemplate.delete(redisKey));
            }
        }
        data.setMessage("success");
        data.setCode(SUCCESSS_CODE);
        data.setData(map);
        return data;
    }




    /**
     * 根据key值获取参数
     * @return
     */
    @GetMapping("getData")
    public Object getData(@RequestBody String key){

        ReturnData data = new ReturnData();
        data.setMessage("查询成功！");
        data.setCode(SUCCESSS_CODE);
        if (redisTemplate.hasKey(key)){
            Object obj = redisTemplate.opsForValue().get(key);
            data.setData(obj);
        }else {
            data.setMessage("未查询到参数！");
            data.setCode(FAIL_CODE);
        }
        return data;
    }
}
