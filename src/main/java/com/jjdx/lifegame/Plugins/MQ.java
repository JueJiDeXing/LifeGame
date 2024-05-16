package com.jjdx.lifegame.Plugins;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 消息队列
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/16 <br> */
public class MQ {

    static Queue<Runnable> queue = new LinkedList<>();
    static Lock lock = new ReentrantLock();
    static boolean run = false;

    public static void addTask(Runnable runnable) {
        lock.lock();
        queue.offer(runnable);
        lock.unlock();
    }

    public static void runTask() {
        while (run) {
            if (!queue.isEmpty()) {
                lock.lock();
                Objects.requireNonNull(queue.poll()).run();
                lock.unlock();
            }
        }
    }

    public static void stopTask() {
        run = false;
    }


}
