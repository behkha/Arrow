package com.ratintech.arrow;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ArrowImageLoader extends AsyncTask<String, Void, Bitmap> {

    private String imageUrl;
    private WeakReference<ImageView> imageViewWeakReference;
    private WeakReference<ArrowImageCache> arrowImageCacheWeakReference;

    public ArrowImageLoader(ImageLoaderBuilder builder) {
        this.imageUrl = builder.imageUrl;
        this.imageViewWeakReference = new WeakReference<>(builder.imageView);
        this.arrowImageCacheWeakReference = new WeakReference<>(builder.arrowImageCache);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {


        if (imageViewWeakReference.get() != null) {
            try {
                if (arrowImageCacheWeakReference.get() != null) {
                    ArrowImageCache arrowImageCache = arrowImageCacheWeakReference.get();
                    if (arrowImageCache.get(imageUrl) != null)
                        return arrowImageCache.get(imageUrl);
                }
                ImageView view = imageViewWeakReference.get();
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                int width = view.getWidth();
                int height = view.getHeight();
                if (width == 0) {
                    return BitmapFactory.decodeStream(bufferedInputStream);
                } else {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();

                    connection = (HttpURLConnection) url.openConnection();
                    inputStream = connection.getInputStream();
                    options.inSampleSize = calculateInSampleSize(options, width, height);
                    options.inJustDecodeBounds = false;
                    return BitmapFactory.decodeStream(inputStream, null , options);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null && imageViewWeakReference.get() != null) {
            imageViewWeakReference.get().setImageBitmap(bitmap);
            if (arrowImageCacheWeakReference.get() != null) {
                ArrowImageCache arrowImageCache = arrowImageCacheWeakReference.get();
                arrowImageCache.put(imageUrl, bitmap);
            }
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static class ImageLoaderBuilder {
        String imageUrl;
        ImageView imageView;
        ArrowImageCache arrowImageCache;

        public ImageLoaderBuilder(ArrowImageCache arrowImageCache) {
            this.arrowImageCache = arrowImageCache;
        }

        public ImageLoaderBuilder setUrl(String url){
            this.imageUrl = url;
            return this;
        }

        public ImageLoaderBuilder setImageView(ImageView imageView){
            this.imageView = imageView;
            return this;
        }

        public void load(){
            new ArrowImageLoader(this).execute();
        }
    }
}
