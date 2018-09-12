package com.derrick;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;

/**
 * Created by Derrick on 2018-09-05.
 * 简单限流
 */
public class SimpleRateLimiter {

    private Jedis jedis;

    public SimpleRateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) throws IOException {
        String key = String.format("hist:%s:%s", userId, actionKey);
        long nowTs = System.currentTimeMillis();
        Pipeline pipe = jedis.pipelined();
        pipe.multi();
        pipe.zadd(key, nowTs, "" + nowTs);
        pipe.zremrangeByScore(key, 0, nowTs - period * 1000);
        Response<Long> count = pipe.zcard(key);
        pipe.expire(key, period + 1);
        pipe.exec();
        pipe.close();
        return count.get() <= maxCount;
    }

    public static void main(String[] args) {
        Jedis jedis = JedisUtils.getJedis();
        SimpleRateLimiter limiter = new SimpleRateLimiter(jedis);
        try {
            for(int i=0;i<20;i++) {
                System.out.println(limiter.isActionAllowed("tom", "reply", 60, 5));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisUtils.closeJedis(jedis);
    }
}
