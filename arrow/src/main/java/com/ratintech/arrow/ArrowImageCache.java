package com.ratintech.arrow;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ArrowImageCache extends LruCache<String, Bitmap> {

    public ArrowImageCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }

}
