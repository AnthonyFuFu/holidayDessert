package com.holidaydessert.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Lock鎖和synchronized比較
 * 100萬以下synchronized優於Lock
 * 100萬以上Lock優於synchronized
 */
@Slf4j
@Service
public class LockService {

    private static final Lock lock = new ReentrantLock();

    // temp 改由外部傳入
    public String execute(int temp) {
        String lockResult = lockDemo(temp);
        String syncResult = syncDemo(temp);
        return lockResult + " | " + syncResult;
    }

    private String lockDemo(int temp) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < temp; i++) {
            final int num = i;
            ((Runnable) () -> lockMethod(num)).run();
        }
        long end = System.currentTimeMillis();
        String result = "Lock 耗時：" + (end - start) + "ms（執行次數：" + temp + "）";
        log.info(result);
        return result;
    }

    private String syncDemo(int temp) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < temp; i++) {
            final int num = i;
            ((Runnable) () -> syncMethod(num)).run();
        }
        long end = System.currentTimeMillis();
        String result = "Synchronized 耗時：" + (end - start) + "ms（執行次數：" + temp + "）";
        log.info(result);
        return result;
    }

    private void lockMethod(int i) {
        lock.lock();
        lock.unlock();
    }

    private synchronized void syncMethod(int i) {
    }
    
}
