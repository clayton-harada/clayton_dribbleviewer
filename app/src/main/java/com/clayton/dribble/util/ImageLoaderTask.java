package com.clayton.dribble.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by clayton on 01/09/16.
 */
public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {
    protected final static Logger log = Logger.getLogger(ImageLoaderTask.class.getName());

    private static LruCache<String, Bitmap> mMemoryCache;

    private String url;
    private ImageView imageView;

    public ImageLoaderTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;

        if(mMemoryCache==null) initImageCache();
    }

    public static ImageLoaderTask getInstance(String url, ImageView imageView) {
        return new ImageLoaderTask(url, imageView);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            Bitmap myBitmap = getBitmapFromMemCache(url);
            if(myBitmap==null) {
                log.info("Loading: " + url);
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
            }
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        addBitmapToMemoryCache(url, result);
        imageView.setImageBitmap(result);
    }

    protected void initImageCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
