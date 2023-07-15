/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mautibla.utils.bitmaps;

import android.content.Context;
import android.graphics.Bitmap;

public abstract class SyncImageWorker {
    protected static final String TAG = "SyncImageWorker";

    private ImageCache mImageCache;
    protected Context mContext;

    protected SyncImageWorker(Context context) {
        mContext = context;
    }

    public Bitmap loadImage(Object data) {
        return doLoadImage(data);
    }
    
	private Bitmap doLoadImage(Object data) {
		Bitmap bitmap = null;

        if (mImageCache != null) {
            bitmap = mImageCache.getBitmapFromMemCache(String.valueOf(data));
        }

        if (bitmap != null) {
            return bitmap;
        }
        
        
        final String dataString = String.valueOf(data);
        

        if (mImageCache != null) {
            bitmap = mImageCache.getBitmapFromDiskCache(dataString);
        }

        if (bitmap == null) {
            bitmap = processBitmap(data);
        }

        if (bitmap != null && mImageCache != null) {
        	mImageCache.addBitmapToCache(dataString, bitmap);
        }

        return bitmap;
	}

    public void setImageCache(ImageCache cacheCallback) {
        mImageCache = cacheCallback;
    }

    public ImageCache getImageCache() {
        return mImageCache;
    }

    protected abstract Bitmap processBitmap(Object data);

}
