package com.jjdx.lifegame.Plugins;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.*;

/**
 日志
 <br>

 @ Author: 绝迹的星 <br>
 @ Time: 2024/5/8 <br> */
public class MyLogger {
    static Logger logger = Logger.getLogger("MyLog");
    static String logFileName = "myLog.log";
    static long MaxSize = 10 * 1024 * 1024;// 10MB
    static long remove = 2 * 1024 * 1024; // 2MB

    static {
        try {
            FileHandler fh = new FileHandler(logFileName, true);
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

    private static void check() {
        //检查文件"myLog.log"的大小
        File file = new File(logFileName);
        long fileSize = file.length();
        if (fileSize < MaxSize) return;
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            byte[] buffer = new byte[1024];
            long remainingBytes = fileSize - remove;
            while (remainingBytes > 0) {
                int bytesRead = raf.read(buffer);
                raf.seek(raf.getFilePointer() - bytesRead);
                raf.write(buffer, 0, bytesRead);
                remainingBytes -= bytesRead;
            }
            raf.setLength(fileSize - remove);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     写入信息到log,自定义级别<br>
     OFF: 关闭日志<br>
     WARNING: 警告<br>
     INFO: 信息<br>
     SEVERE: 严重<br>
     CONFIG: 配置<br>
     FINE: 详细信息<br>
     FINER: 更详细的信息<br>
     FINEST: 极其详细的信息<br>
     ALL: 所有信息<br>
     */
    public static void log(Level level, String info) {
        check();
        logger.log(level, info);
    }

    /**
     写入信息到log, 级别OFF
     */
    public static void off(String info) {
        log(Level.OFF, info);
    }

    /**
     写入信息到log, 级别WARNING
     */
    public static void warning(String info) {
        log(Level.WARNING, info);
    }

    /**
     写入信息到log, 级别INFO
     */
    public static void info(String info) {
        log(Level.INFO, info);
    }

    /**
     写入信息到log, 级别SEVERE
     */
    public static void severe(String info) {
        log(Level.SEVERE, info);
    }

    /**
     写入信息到log, 级别CONFIG
     */
    public static void config(String info) {
        log(Level.CONFIG, info);
    }

    /**
     写入信息到log, 级别FINE
     */
    public static void fine(String info) {
        log(Level.FINE, info);
    }

    /**
     写入信息到log, 级别FINER
     */
    public static void finer(String info) {
        log(Level.FINER, info);
    }

    /**
     写入信息到log, 级别FINEST
     */
    public static void finest(String info) {
        log(Level.FINEST, info);
    }

}
