package com.jjdx.lifegame.Plugins;

import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

/**
 日志
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/8 <br> */
public class MyLogger {
    static Logger logger = Logger.getLogger("MyLog");

    static {
        try {
            FileHandler fh = new FileHandler("myLog.log", true);
            fh.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("%s: %s%n", record.getLevel(), record.getMessage());
                }
            });
            logger.addHandler(fh);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private MyLogger() {
    }

    public void setLevel(Level level) {
        logger.setLevel(level);
    }

    /**
     写入信息到log,自定义级别
     */
    public static void log(Level level, String info) {
        logger.log(level, info);
    }

    /**
     写入信息到log, 级别WARNING
     */
    public static void warn(String info) {
        logger.log(Level.WARNING, info);
    }

    /**
     写入信息到log, 级别INFO
     */
    public static void info(String info) {
        logger.log(Level.INFO, info);
    }

    /**
     写入信息到log, 级别SEVERE
     */
    public static void severe(String info) {
        logger.log(Level.SEVERE, info);
    }

    /**
     写入信息到log, 级别CONFIG
     */
    public static void config(String info) {
        logger.log(Level.CONFIG, info);
    }

    /**
     写入信息到log, 级别FINE
     */
    public static void fine(String info) {
        logger.log(Level.FINE, info);
    }

    /**
     写入信息到log, 级别FINER
     */
    public static void finer(String info) {
        logger.log(Level.FINER, info);
    }

    /**
     写入信息到log, 级别FINEST
     */
    public static void finest(String info) {
        logger.log(Level.FINEST, info);
    }

}
