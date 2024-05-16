package com.jjdx.lifegame.Plugins;

/**
 简易计时器
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/16 <br> */
public class Tick {
    public long lastTick = 0;

    public long now() {
        return System.currentTimeMillis();
    }

    public void setNow() {
        lastTick = now();
    }

    public long distance() {
        return now() - lastTick;
    }
}
