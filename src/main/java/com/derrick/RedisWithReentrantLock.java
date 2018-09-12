package com.derrick;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Derrick on 2018-08-30.
 * Redis版本的可重入锁
 */
public class RedisWithReentrantLock {

    private ThreadLocal<Map<String, Integer>> locks = new ThreadLocal();

    private Jedis jedis;

    public RedisWithReentrantLock(Jedis jedis){
        this.jedis = jedis;
    }

    private Map<String, Integer> currentLockers() {
        Map<String, Integer> refs = locks.get();
        if (refs != null) {
            return refs;
        }
        locks.set(new HashMap<String, Integer>());
        return locks.get();
    }

    public boolean lock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt != null) {
            refs.put(key, refCnt + 1);
            return true;
        }
        //this line is equal to "set lock:key true ex 5 nx"
        boolean ok = jedis.set(key, "", "nx", "ex", 5L) != null;
        if (!ok) {
            return false;
        }
        refs.put(key, 1);
        return true;
    }

    public boolean unlock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt == null) {
            return false;
        }
        refCnt -= 1;
        if (refCnt > 0) {
            refs.put(key, refCnt);
        } else {
            refs.remove(key);
            jedis.del(key);
        }
        return true;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        RedisWithReentrantLock redis = new RedisWithReentrantLock(jedis);
        System.out.println(redis.lock("codehole"));
        System.out.println(redis.lock("codehole"));
        System.out.println(redis.unlock("codehole"));
        System.out.println(redis.unlock("codehole"));
    }

}
