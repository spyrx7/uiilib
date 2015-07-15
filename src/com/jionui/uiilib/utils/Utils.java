package com.jionui.uiilib.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	private static final String Util_LOG = makeLogTag(Utils.class);

    public static String makeLogTag(Class<?> cls) {
        return cls.getName();
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * �?��是否存在SD�?
     * 
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建目录
     * 
     * @param context
     * @param dirName
     *            文件夹名�?
     * @return
     */
    public static File createFileDir(Context context, String dirName) {
        String filePath;
        // 如SD卡已存在，则存储；反之存在data目录�?
        if (hasSdcard()) {
            // SD卡路�?
            filePath = Environment.getExternalStorageDirectory()
                    + File.separator + dirName;
        } else {
            filePath = context.getCacheDir().getPath() + File.separator
                    + dirName;
        }
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            boolean isCreate = destDir.mkdirs();
            Log.i(Util_LOG, filePath + " has created. " + isCreate);
        }
        return destDir;
    }

    /**
     * 删除文件（若为目录，则�?归删除子目录和文件）
     * 
     * @param file
     * @param delThisPath
     *            true代表删除参数指定file，false代表保留参数指定file
     */
    public static void delFile(File file, boolean delThisPath) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                int num = subFiles.length;
                // 删除子目录和文件
                for (int i = 0; i < num; i++) {
                    delFile(subFiles[i], true);
                }
            }
        }
        if (delThisPath) {
            file.delete();
        }
    }

    /**
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     * 
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    int num = subFiles.length;
                    for (int i = 0; i < num; i++) {
                        size += getFileSize(subFiles[i]);
                    }
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 保存Bitmap到指定目�?
     * 
     * @param dir
     *            目录
     * @param fileName
     *            文件�?
     * @param bitmap
     * @throws IOException
     */
    public static void savaBitmap(File dir, String fileName, Bitmap bitmap) {
    	Log.d("Utils", "savaBitmap:"+dir+fileName+"|"+bitmap);
        if (bitmap == null) {
            return;
        }
        File file = new File(dir, fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
        	Log.d("Utils", "savaBitmap IOException:"+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 判断某目录下文件是否存在
     * 
     * @param dir
     *            目录
     * @param fileName
     *            文件�?
     * @return
     */
    public static boolean isFileExists(File dir, String fileName) {
        return new File(dir, fileName).exists();
    }
}
