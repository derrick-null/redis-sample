package com.derrick;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Derrick on 2018-08-30.
 * hello world
 */
public class HelloRedis {

    /**
     * JedisPool is thread safe
     * */
    private static JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),"192.168.200.80",6382);

    public static void main(String[] args){

        HelloRedis helloRedis = new HelloRedis();

        Jedis jedis = jedisPool.getResource();
        jedis.set("greeting", "hello world");
        System.out.println(jedis.get("greeting"));
        jedis.close();

        helloRedis.jsonSample();

        jedisPool.close();
    }

    private void jsonSample(){
        Jedis jedis = jedisPool.getResource();
        UserVO userVO = new UserVO();
        userVO.setId(1L);
        userVO.setName("Tom");
        userVO.setGender("male");
        userVO.setAge(22);
        jedis.set(String.valueOf(userVO.getId()), JSON.toJSONString(userVO));
        String serializationStr = jedis.get(String.valueOf(userVO.getId()));
        JSONObject resultVO = (JSONObject) JSON.parse(serializationStr);
        System.out.println(resultVO.toJSONString());
        jedis.close();
        jedisPool.close();
    }
}
