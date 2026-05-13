package com.holidaydessert.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConcurrentSemaphoreService {

    // clientTotal 和 threadTotal 改由外部傳入
    public String execute(int clientTotal, int threadTotal) throws Exception {
        AtomicInteger count = new AtomicInteger(0);

        ExecutorService executorService = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadTotal);
        CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    count.incrementAndGet();
                    log.info("線程:[{}] count:{}", Thread.currentThread().getName(), count.get());
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        String result = "Semaphore 測試完成，clientTotal:" + clientTotal +
                        " threadTotal:" + threadTotal +
                        " 最終 count：" + count.get();
        log.info(result);
        return result;
    }
    
}