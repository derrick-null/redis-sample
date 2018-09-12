package com.derrick;

import redis.clients.jedis.Client;

/**
 * Created by Derrick on 2018-09-05.
 * 布隆过滤器
 * 需要Redis4.0
 * Jedis-2.x 没有提供指令扩展机制，所以无法直接使用Jedis来访问 Redis Module 提供的 bf.xxx 指令。
 * RedisLabs 提供了一个单独的包 JReBloom，但是要Jedis3.0才开始支持
 */
public class BloomFilterTest {

    public static void main(String[] arg){
        /*Client client = new Client();

        client.delete("codehole");
        for (int i = 0; i < 100000; i++) {
            client.add("codehole", "user" + i);
            boolean ret = client.exists("codehole", "user" + (i + 1));
            if (ret) {
                System.out.println(i);
                break;
            }
        }

        client.close();*/
    }
}
