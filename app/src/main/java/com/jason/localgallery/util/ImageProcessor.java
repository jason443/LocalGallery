package com.jason.localgallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by jason on 2016/8/4.
 *
 */
public class ImageProcessor {

    /**
     * 通过传进来的字节数组压缩图片
     * @param bytes Bitmap的字符数组
     * @param reqWith 压缩后的宽度
     * @param reqHeight 压缩后的高度
     * @return 压缩后的图片
     */
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

    /**
     * 计算采样率
     * @param options 保存着原图片的高和宽
     * @param reqWidth 压缩后的宽
     * @param reqHeight 压缩后的高
     * @return 计算出来的采样率
     */
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

    /** 从质量压缩bitmap
     * @param image 压缩前的Bitmap
     * @return 压缩后的Bitmap
     */
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
        return BitmapFactory.decodeStream(baiStream);
    }

}
