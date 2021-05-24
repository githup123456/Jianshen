package com.shyj.jianshen;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class MyCrashHandler implements Thread.UncaughtExceptionHandler {
    private static MyCrashHandler crashHandler;

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // TODO Auto-generated method stub
        if (crashHandler != null) {
            try {
                //将crash log写入文件
                FileOutputStream fileOutputStream = new FileOutputStream("/mnt/sdcard/crash_log.txt", true);
                PrintStream printStream = new PrintStream(fileOutputStream);
                ex.printStackTrace(printStream);
                printStream.flush();
                printStream.close();
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }

        }

    }
    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    private MyCrashHandler() {
    }
    //单例
    public static MyCrashHandler instance() {
        if (crashHandler == null) {
            synchronized (crashHandler) {
                crashHandler = new MyCrashHandler();
            }
        }
        return crashHandler;
    }

}