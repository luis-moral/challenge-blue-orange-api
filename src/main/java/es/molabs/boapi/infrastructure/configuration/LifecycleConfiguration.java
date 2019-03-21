package es.molabs.boapi.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import javax.annotation.PreDestroy;

@Component
public class LifecycleConfiguration {

    @Autowired
    private JedisPool redisClientPool;

    @PreDestroy
    public void onPreDestroy() {
        redisClientPool.close();
    }
}
