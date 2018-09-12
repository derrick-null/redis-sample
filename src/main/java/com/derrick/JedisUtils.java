package com.derrick;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 *
 * @author Derrick
 * @date 2018-09-05
 * Jedis工具类，获取jedis client
 */
public class JedisUtils {

    private static final String IP = "192.168.200.80";

    private static final int PORT = 6382;

    private static JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(jedisPoolConfig, IP, PORT);
    }

    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    public static void closeJedis(Jedis jedis){
        if(null != jedis){
            jedis.close();
        }
    }
}
