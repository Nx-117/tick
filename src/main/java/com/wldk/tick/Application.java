package com.wldk.tick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@SpringBootApplication
public class Application {

    private RedisTemplate<Object,Object> redisTemplate = null;
    private RedisConnectionFactory connectionFactory = null;
    private MessageListener redisMsgListener = null;

    @Autowired
    public Application(RedisTemplate<Object, Object> redisTemplate, RedisConnectionFactory connectionFactory, MessageListener redisMsgListener) {
        this.redisTemplate = redisTemplate;
        this.connectionFactory = connectionFactory;
        this.redisMsgListener = redisMsgListener;
    }

    /**
     * 任务池
     */
    private ThreadPoolTaskScheduler taskScheduler = null;

    /**
     * 创建任务池，运行线程等待处理Redis的消息
     * @return  任务池
     */
    @Bean
    public ThreadPoolTaskScheduler initTaskScheduler() {
        if (taskScheduler != null) {
            return taskScheduler;
        }
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return taskScheduler;
    }

    /**
     * 定义Redis的监听容器
     * @return 监听容器
     */
    @Bean
    public RedisMessageListenerContainer initRedisContainer() {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        // Redis连接工厂
        container.setConnectionFactory(connectionFactory);
        // 设置运行任务池
        container.setTaskExecutor(initTaskScheduler());
        // 定义监听渠道，名称为topic1
        Topic topicTest = new ChannelTopic("topic1");
        // 针对键过期事件定义监听渠道
        Topic topicEventOfExpire = new ChannelTopic("__keyevent@0__:expired");
        Topic topicEventOfSet = new ChannelTopic("__keyevent@0__:set");
        // 针对具体的键定义监听渠道
        Topic topicSpace = new ChannelTopic("__keyspace@0__:key1");
        // 针对正则匹配的键定义监听渠道
        Topic topicSpaceOfPattern = new PatternTopic("__keyevent@0__:notify*");

        Collection<Topic> topicCollection = new ArrayList<>();
        topicCollection.add(topicTest);
        topicCollection.add(topicEventOfExpire);
        topicCollection.add(topicEventOfSet);
        topicCollection.add(topicSpace);
        topicCollection.add(topicSpaceOfPattern);

        // 使用监听器监听Redis的消息
        container.addMessageListener(redisMsgListener,topicCollection);
        return container;
    }

    @PostConstruct
    public void init() {
        initRedisTemplate();
    }

    /**
     * 设置 RedisTemplate 的序列化器
     */
    private void initRedisTemplate() {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
