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
public class PictureProcessor {

    public static Bitmap compressBitmapFromStream(InputStream is, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Log.d("Main2", BitmapFactory.decodeStream(is).getByteCount() + "?");
        Log.d("Main3", BitmapFactory.decodeStream(is, null, options).getByteCount() + "?");
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        Log.d("Main4", bitmap.getByteCount() + "?");
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

    public static Bitmap compressBitmapFromBitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Log.d("Main1", image.getByteCount() + "");
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(bais);
        Log.d("Main1", bitmap.getByteCount() + "");
        return bitmap;
    }

}
