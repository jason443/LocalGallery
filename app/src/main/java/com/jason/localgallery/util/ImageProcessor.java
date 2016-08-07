package com.jason.localgallery.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jason on 2016/8/4.
 */
public class ImageProcessor {

    public Bitmap compressBitmapFromBytes(byte[] bytes, int reqWith, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWith, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmap = compressBitmapFromBitmap(bitmap);
        return bitmap;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqHeight == 0 || reqWidth == 0) {
            return 1;
        }

        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqHeight) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize >= reqHeight) && (halfWidth / inSampleSize >= reqWidth)) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap compressBitmapFromBitmap(Bitmap image) {
        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baoStream);
        int options = 100;
        while (baoStream.toByteArray().length / 1024 > 32) {
            baoStream.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, baoStream);
        }
        ByteArrayInputStream baiStream = new ByteArrayInputStream(baoStream.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(baiStream);
        return bitmap;
    }

}
