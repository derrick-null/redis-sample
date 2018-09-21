package com.derrick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Derrick on 2018-09-21.
 * PubSubSample
 */
public class PubSubSample {

    Logger logger = LoggerFactory.getLogger(PubSubSample.class);

    private ArrayList<String> messageContainer = new ArrayList<String>();

    private CountDownLatch messageReceivedLatch = new CountDownLatch(1);
    private CountDownLatch publishLatch = new CountDownLatch(1);

    public static void main(String[] args){
        try {
            new PubSubSample().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void run() throws InterruptedException {
        setUpPublisher();
        JedisPubSub jedisPubSub = setUpSubscriber();

        // publish away!
        publishLatch.countDown();

        messageReceivedLatch.await();
        logger.info("Got message: " + messageContainer.iterator().next());

        jedisPubSub.unsubscribe();
    }

    private JedisPubSub setUpSubscriber(){
        final JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void subscribe(String... channels) {
                super.subscribe(channels);
                logger.info("subscribe: " + channels);
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                super.onSubscribe(channel, subscribedChannels);
                logger.info("onSubscribe: " + channel);
            }

            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);
                messageContainer.add(message);
                logger.info("onMessage() channel: " + channel + ", message: " + message);
                messageReceivedLatch.countDown();
            }

            @Override
            public void unsubscribe() {
                super.unsubscribe();
                logger.info("unsubscribe");
            }

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                super.onUnsubscribe(channel, subscribedChannels);
                logger.info("onUnsubscribe: " + channel);
            }

        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Connecting");
                    Jedis jedis = JedisUtils.getJedis();
                    logger.info("subscribing");
                    jedis.subscribe(jedisPubSub, "test");
                    logger.info("subscribe returned, closing down");
                    JedisUtils.closeJedis(jedis);
                } catch (Exception e) {
                    logger.error("subscribe error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, "subscriberThread").start();
        return jedisPubSub;
    }

    private void setUpPublisher(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Jedis jedis = JedisUtils.getJedis();
                    logger.info("waiting for publish");
                    publishLatch.await();
                    logger.info("ready to publish, waiting for 1 sec");
                    Thread.sleep(1000);
                    logger.info("publishing");
                    jedis.publish("test", "this is a test message");
                    logger.info("published");
                    JedisUtils.closeJedis(jedis);
                }catch (Exception e){
                    logger.error("Publish error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, "publisherThread").start();
    }
}
