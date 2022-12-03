package com.limnac.musicplayer.utils;
import android.annotation.SuppressLint;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * @author limnac
 * @email carl.hu@live.cn
 * @date 2022/11/26 10:54
 * @description com.limnac.musicplayer
 */

public class LogUtil {

    // 日志文件总数
    private static final int LOG_FILE_TOTAL = 10;
    // 单日志文件大小上限 MB
    private static final long LOG_SIZE_MAX = 10;
    // 日志文件输出文件夹
    @SuppressLint("SdCardPath")
    private static final String LOG_FILE_PRINT_DIR = "/sdcard/limnac/com.limnac.musicplayer/log/";

    // 文件名格式
    private static final SimpleDateFormat FILE_NAME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");

    private static final Object LOCK = new Object();


    public static final void d(String TAG, String msg) {
        synchronized (LOCK) {
            Log.d(TAG, msg);
            printLog("DEBUG", TAG, msg);
        }
    }

    public static final void i(String TAG, String msg) {
        synchronized (LOCK) {
            Log.i(TAG, msg);
            printLog("INFO ", TAG, msg);
        }
    }

    public static final void w(String TAG, String msg) {
        synchronized (LOCK) {
            Log.w(TAG, msg);
            printLog("WARN ", TAG, msg);
        }
    }

    public static final void e(String TAG, String msg) {
        synchronized (LOCK) {
            Log.e(TAG, msg);
            printLog("ERROR", TAG, msg);
        }
    }

    public static final void error(Exception e) {
        synchronized (LOCK) {
            e.printStackTrace();
            printStackTrace(e);
        }
    }

    public static final void throwable(Throwable throwable) {
        synchronized (LOCK) {
            throwable.printStackTrace();
            printStackTrace(throwable);
        }
    }

    private static void printLog(String level, String TAG, String msg) {
        try {
            FileWriter fileWriter = new FileWriter(getFile(), true);
            fileWriter.write(formatLog(level, TAG, msg));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printStackTrace(Exception exception) {
        try {
            StringWriter errorsWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(errorsWriter));
            FileWriter fileWriter = new FileWriter(getFile(), true);
            fileWriter.write(formatLog("ERROR", "System.err", errorsWriter.toString()));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printStackTrace(Throwable throwable) {
        try {
            StringWriter errorsWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(errorsWriter));
            FileWriter fileWriter = new FileWriter(getFile(), true);
            fileWriter.write(formatLog("THROWABLE", "System.throwable", errorsWriter.toString()));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //格式化日志
    private static String formatLog(String level, String TAG, String msg) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat.format(new Date(System.currentTimeMillis())) + " ");
        builder.append("[" + level + "] ");
        builder.append("[" + TAG + "] ");
        builder.append(msg);
        builder.append("\n");
        return builder.toString();
    }

    // 获取需要输出的日志文件
    private static File getFile() {

        // 确认文件夹是否存在
        File fileDir = new File(LOG_FILE_PRINT_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        // 获取文件夹下的日志文件
        File[] fileList = fileDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".log");
            }
        });
        int fileCount = fileList == null ? 0 : fileList.length;

        // 没有日志文件时，直接创建新文件
        if (fileCount == 0) {
            return createLogFile();
        }

        // 只有一个日志文件时
        if (fileCount == 1) {
            return isCreateLogFile(fileList[0]);
        }

        // 对日志排序，排序结果为升序
        Arrays.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                String file1Name = "";
                String file2Name = "";
                try {
                    file1Name = file1.getName().split(".log")[0];
                    file2Name = file2.getName().split(".log")[0];

                    Date dateFile1 = FILE_NAME_FORMAT.parse(file1Name);
                    Date dateFile2 = FILE_NAME_FORMAT.parse(file2Name);
                    return dateFile1.getTime() < dateFile2.getTime() ? -1 : 1;
                } catch (Exception e) {
                    Log.i("LogUtil", "file1Name:" + file1Name + ",   file2Name:" + file2Name);
                    e.printStackTrace();

                }
                return 0;
            }
        });
        File lastFile = fileList[fileCount - 1];
        // 日志文件未超过最大控制个数
        if (fileCount <= LOG_FILE_TOTAL) {
            return isCreateLogFile(lastFile);
        }

        // 删除时间最早的一个文件
        fileList[0].delete();
        return isCreateLogFile(lastFile);
    }


    // 确认是否需要创建新日志文件
    private static File isCreateLogFile(File file) {
        // 超过日志文件大小上限，需要创建新日志文件
        if (sizeOf(file) >= LOG_SIZE_MAX) {
            return createLogFile();
        }
        return file;
    }


    // 创建一个新的日志文件
    private static File createLogFile() {
        return new File(LOG_FILE_PRINT_DIR + FILE_NAME_FORMAT.format(new Date(System.currentTimeMillis())) + ".log");
    }

    // 计算文件大小，返回单位 MB
    private static long sizeOf(File file) {
        long length = file.length();
        return length / 1024 / 1024;
    }

}
