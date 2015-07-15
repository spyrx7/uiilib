package com.jionui.uiilib.utils;

import java.io.File;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

public class ImageDownLoader {
    private static final String ImageDownLoader_Log = Utils
            .makeLogTag(ImageDownLoader.class);

    /** 保存正在下载或等待下载的URL和相应失败下载次数（初始�?），防止滚动时多次下�?*/
    private Hashtable<String, Integer> taskCollection;
    /** 缓存�?*/
    private LruCache<String, Bitmap> lruCache;
    /** 线程�?*/
    private ExecutorService threadPool;
    /** 缓存文件目录 （如无SD卡，则data目录下） */
    private File cacheFileDir;
    /** 缓存文件�?*/
    private static final String DIR_CACHE = ".diandian_cache";
    /** 缓存文件夹最大容量限制（10M�?*/
    private static final long DIR_CACHE_LIMIT = 10 * 1024 * 1024;
    /** 图片下载失败重试次数 */
    private static final int IMAGE_DOWNLOAD_FAIL_TIMES = 2;

    @SuppressLint("NewApi") 
    public ImageDownLoader(Context context) {
        // 获取系统分配给每个应用程序的�?��内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 给LruCache分配�?��内存�?/8
        lruCache = new LruCache<String, Bitmap>(maxMemory / 8) {
            // 必须重写此方法，来测量Bitmap的大�?
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        taskCollection = new Hashtable<String, Integer>();
        // 创建线程�?
        threadPool = Executors.newFixedThreadPool(10);
        cacheFileDir = Utils.createFileDir(context, DIR_CACHE);
    }

    /**
     * 添加Bitmap到内存缓�?
     * 
     * @param key
     * @param bitmap
     */
    @SuppressLint("NewApi") private void addLruCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            lruCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中获取Bitmap
     * 
     * @param key
     * @return
     */
    @SuppressLint("NewApi") private Bitmap getBitmapFromMemCache(String key) {
        return lruCache.get(key);
    }

    /**
     * 异步下载图片，并按指定宽度和高度压缩图片
     * 
     * @param url
     * @param width
     * @param height
     * @param listener
     *            图片下载完成后调用接�?
     */
    public void loadImage(final String url, final int width, final int height,
            AsyncImageLoaderListener listener) {
        Log.i(ImageDownLoader_Log, "loadImage:" + url);
        final ImageHandler handler = new ImageHandler(listener);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i(ImageDownLoader_Log, "loadImage run:" + url);
                Bitmap bitmap = downloadImage(url, width, height);
                Message msg = handler.obtainMessage();
                msg.obj = bitmap;
                handler.sendMessage(msg);
                // 将Bitmap 加入内存缓存
                addLruCache(url, bitmap);
                // 加入文件缓存前，�?��断缓存目录大小是否超过限制，超过则清空缓存再加入
                long cacheFileSize = Utils.getFileSize(cacheFileDir);
                if (cacheFileSize > DIR_CACHE_LIMIT) {
                    Log.i(ImageDownLoader_Log, cacheFileDir
                            + " size has exceed limit." + cacheFileSize);
                    Utils.delFile(cacheFileDir, false);
                    taskCollection.clear();
                }
                // 缓存文件名称�?替换url中非字母和非数字的字符，防止系统误认为文件路径）
                String urlKey = url.replaceAll("[^\\w]", "");
                // 将Bitmap加入文件缓存
                Utils.savaBitmap(cacheFileDir, urlKey, bitmap);
            }
        };
        // 记录该url，防止滚动时多次下载�?代表该url下载失败次数
        taskCollection.put(url, 0);
        threadPool.execute(runnable);
    }

    /**
     * 获取Bitmap, 若内存缓存为空，则去文件缓存中获�?
     * 
     * @param url
     * @return 若缓存中没找到，则返回null
     */
    public Bitmap getBitmapCache(String url) {
        // 去处url中特殊字符作为文件缓存的名称
        String urlKey = url.replaceAll("[^\\w]", "");
        if (getBitmapFromMemCache(url) != null) {
            return getBitmapFromMemCache(url);
        } else if (Utils.isFileExists(cacheFileDir, urlKey)
                && Utils.getFileSize(new File(cacheFileDir, urlKey)) > 0) {
            // 从文件缓存中获取Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(cacheFileDir.getPath()
                    + File.separator + urlKey);
            // 将Bitmap 加入内存缓存
            addLruCache(url, bitmap);
            return bitmap;
        }
        return null;
    }

    /**
     * 下载图片，并按指定高度和宽度压缩
     * 
     * @param url
     * @param width
     * @param height
     * @return
     */
    private Bitmap downloadImage(String url, int width, int height) {
        Bitmap bitmap = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            httpClient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                //解决缩放大图时出现SkImageDecoder::Factory returned null错误
                byte[] byteIn = EntityUtils.toByteArray(entity);
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(byteIn, 0, byteIn.length,
                        bmpFactoryOptions);
                Log.d("ImageDownLoader", "downloadImage:"+bmpFactoryOptions.outHeight+"|"+height+"|"+bmpFactoryOptions.outWidth+"|"+width);
                /*int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                        / height);
                int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                        / width);
                if (heightRatio > 1 && widthRatio > 1) {
                    bmpFactoryOptions.inSampleSize = heightRatio > widthRatio ? heightRatio
                            : widthRatio;
                }*/
                bmpFactoryOptions.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(byteIn, 0,
                        byteIn.length, bmpFactoryOptions);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        // 下载失败，再重新下载
        // 本例是图片下载失败则再次下载，可根据�?��改变，比如记录下载失败的图片URL，在某个时刻再次下载
        if (taskCollection.get(url) != null) {
            int times = taskCollection.get(url);
            if (bitmap == null
                    && times < IMAGE_DOWNLOAD_FAIL_TIMES) {
                times++;
                taskCollection.put(url, times);
                bitmap = downloadImage(url, width, height);
                Log.i(ImageDownLoader_Log, "Re-download " + url + ":" + times);
            }
        }
        return bitmap;
    }

    /**
     * 取消正在下载的任�?
     */
    public synchronized void cancelTasks() {
        if (threadPool != null) {
            threadPool.shutdownNow();
            threadPool = null;
        }
    }

    /**
     * 获取任务列表
     * 
     * @return
     */
    public Hashtable<String, Integer> getTaskCollection() {
        return taskCollection;
    }

    /** 异步加载图片接口 */
    public interface AsyncImageLoaderListener {
        void onImageLoader(Bitmap bitmap);
    }

    /** 异步加载完成后，图片处理 */
    static class ImageHandler extends Handler {

        private AsyncImageLoaderListener listener;

        public ImageHandler(AsyncImageLoaderListener listener) {
            this.listener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listener.onImageLoader((Bitmap) msg.obj);
        }
    }
}

