package com.derrick;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Created by Derrick on 2018-09-12.
 * Watch Sample
 */
public class WatchSample {

    public static void main(String[] str){
        Jedis jedis = JedisUtils.getJedis();
        String userId = "abc";
        String key = keyFor(userId);
        jedis.setnx(key, String.valueOf(5));
        System.out.println(doubleAccount(jedis, userId));
        JedisUtils.closeJedis(jedis);
    }


    public static int doubleAccount(Jedis jedis, String userId) {
        String key = keyFor(userId);
        while (true) {
            jedis.watch(key);
            int value = Integer.parseInt(jedis.get(key));
            value *= 2;
            Transaction tx = jedis.multi();
            tx.set(key, String.valueOf(value));
            List<Object> res = tx.exec();
            if (res != null) {
                break; // 成功了
            }else {
                System.out.println("failed");
            }
        }
        return Integer.parseInt(jedis.get(key));
    }

    public static String keyFor(String userId) {
        return String.format("account_%s", userId);
    }
}
