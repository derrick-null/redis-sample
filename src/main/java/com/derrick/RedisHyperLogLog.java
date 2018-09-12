package com.derrick;

import com.sun.deploy.util.SessionState;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

/**
 * Created by Derrick on 2018-09-05.
 * HyperLogLog使用
 */
public class RedisHyperLogLog {

    public static void main(String[] args) {
        RedisHyperLogLog redisHyperLogLog = new RedisHyperLogLog();
        //redisHyperLogLog.defaultHyperLogLogSample();
        redisHyperLogLog.deviationRateTest();
    }

    /**
    *默认的HyperLogLog使用，会出现误差
    */
    private void defaultHyperLogLogSample(){
        Jedis jedis = JedisUtils.getJedis();
        for (int i = 0; i < 100; i++) {
            jedis.pfadd("hlog", "user" + i);
            long total = jedis.pfcount("hlog");
            if (total != i + 1) {
                System.out.printf("%d %d\n", total, i + 1);
                break;
            }
        }
        JedisUtils.closeJedis(jedis);
    }

    /**
     * it will takes about 2 minutes
     * */
    private void deviationRateTest(){
        Jedis jedis = JedisUtils.getJedis();
        for (int i = 0; i < 100000; i++) {
            jedis.pfadd("hlog", "user" + i);
        }
        long total = jedis.pfcount("hlog");
        System.out.printf("%d %d\n", 100000, total);
        JedisUtils.closeJedis(jedis);
    }
}
