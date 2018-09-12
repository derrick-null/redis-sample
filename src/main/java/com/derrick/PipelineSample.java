package com.derrick;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * Created by Derrick on 2018-09-12.\
 * 管道Sample
 */
public class PipelineSample {

    public static void main(String[] str){
        PipelineSample.execSample();
        PipelineSample.discardSample();
    }

    public static void execSample(){
        Jedis jedis = JedisUtils.getJedis();
        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();
        pipeline.set("execSample1", "tom");
        pipeline.set("execSample2","jerry");
        pipeline.set("execSample3","alice");
        pipeline.exec();
        JedisUtils.closeJedis(jedis);
    }

    public static void discardSample(){
        Jedis jedis = JedisUtils.getJedis();
        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();
        pipeline.set("discardSample1", "tom");
        pipeline.set("discardSample2","jerry");
        pipeline.set("discardSample3","alice");
        pipeline.discard();
        JedisUtils.closeJedis(jedis);
    }
}


