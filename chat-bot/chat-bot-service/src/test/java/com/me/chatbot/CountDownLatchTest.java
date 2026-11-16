package com.me.chatbot;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);

        Runnable runnable = () -> {
            int sleepTime = (int) (Math.random() * 10000);
            try {
                Thread.sleep(sleepTime);
                System.out.println("任务执行完成, 执行时间 " + sleepTime + " ms");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                // 任务执行完成后, 释放一个信号量
                countDownLatch.countDown();
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }

        // 等待所有任务执行完成
        countDownLatch.await();
        System.out.println("所有任务执行完成");
    }

}
