package com.nexio.cws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 保存websocket session id 在 redis
 */
@Component
public class RedisSessionService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // key = 登錄用户名稱， value=websocket的sessionId
    private ConcurrentHashMap<String,String> redisHashMap = new ConcurrentHashMap<>(32);

    /**
     * 在緩存中保存用户和websocket sessionid的信息
     * @param name
     * @param wsSessionId
     */
    public void add(String name, String wsSessionId){
        BoundValueOperations<String,String> boundValueOperations = redisTemplate.boundValueOps(name);
        boundValueOperations.set(wsSessionId,24 * 3600, TimeUnit.SECONDS);
    }

    /**
     * 從緩存中刪除用户的信息
     * @param name
     */
    public boolean delete(String name){
        return redisTemplate.execute(connection -> {
            try {
                byte[] rawKey = redisTemplate.getStringSerializer().serialize(name);
                return connection.del(rawKey) > 0;
            }catch(NullPointerException npe){
                return false;
            }
        }, true);
    }

    /**
     * 根據用户id獲取用户對應的sessionId值
     * @param name
     * @return
     */
    public String get(String name){
        BoundValueOperations<String,String> boundValueOperations = redisTemplate.boundValueOps(name);
        return boundValueOperations.get();
    }
}