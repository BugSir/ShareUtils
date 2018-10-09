package com.bugsir.share.wx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *@author: BUG SIR
 *@date: 2018/10/9 16:27
 *@description: 
 */
public    class BitmapUtil   {
    private static final BitmapFactory.Options DEFAULT_OPTIONS;
    static
    {
        DEFAULT_OPTIONS = new BitmapFactory.Options();
        DEFAULT_OPTIONS.inPurgeable = true;
        DEFAULT_OPTIONS.inInputShareable = true;
        DEFAULT_OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;
    }
    /**
     * 从二进制数据里获取Bitmap
     *
     * @param value 二进制数据
     * @return
     */
    public static Bitmap getBitmap(byte[] value)
    {
        return getBitmap(value, DEFAULT_OPTIONS);
    }

    /**
     * 从二进制数据里获取Bitmap
     *
     * @param value   二进制数据
     * @param options Bitmap配置
     * @return
     */
    public static Bitmap getBitmap(byte[] value, BitmapFactory.Options options)
    {
        if (value == null || value.length < 1)
        {
            return null;
        }
        Bitmap bitmap;
        try
        {
            bitmap = BitmapFactory.decodeByteArray(value, 0, value.length, options);
        } catch (OutOfMemoryError e)
        {
            try
            {
                bitmap = BitmapFactory.decodeByteArray(value, 0, value.length, options);
            } catch (Throwable e2)
            {
                bitmap = null;
            }
        } catch (Exception e)
        {
            bitmap = null;
        }
        value = null;
        return bitmap;
    }
    public static Bitmap createBitmap(Bitmap srcBitmap, int x, int y, int width, int height, Matrix matrix, boolean isRecycle)
    {
        if (srcBitmap == null)
        {
            return srcBitmap;
        }
        try
        {
            Bitmap bitmap = Bitmap.createBitmap(srcBitmap, x, y, width, height, matrix, false);
            if (bitmap == null)
            {
                return srcBitmap;
            }
            if (isRecycle && !srcBitmap.isRecycled())
            {
                srcBitmap.recycle();
            }
            return bitmap;
        } catch (OutOfMemoryError e)
        {
            try
            {
                Bitmap bitmap = Bitmap.createBitmap(srcBitmap, x, y, width, height, matrix, false);
                if (bitmap == null)
                {
                    return srcBitmap;
                }
                if (isRecycle && !srcBitmap.isRecycled())
                {
                    srcBitmap.recycle();
                }
                return bitmap;
            } catch (Throwable e2)
            {
                return srcBitmap;
            }
        } catch (Exception e)
        {
            return srcBitmap;
        }
    }

    private static Bitmap createBitmap(Bitmap srcBitmap, int width, int height, Matrix matrix, boolean isRecycle)
    {
        if (srcBitmap == null)
        {
            return srcBitmap;
        }
        if (width == 0)
        {
            width = srcBitmap.getWidth();
        }
        if (height == 0)
        {
            height = srcBitmap.getHeight();
        }
        return createBitmap(srcBitmap, 0, 0, width, height, matrix, isRecycle);
    }

    /**
     * 把Bitmap压缩成二进制数组，默认使用JPEG格式并回收Bitmap
     *
     * @param bitmap
     * @param maxKBSize 图片最大体积，单位为KB
     * @return
     */
    public static byte[] compress2Array(Bitmap bitmap, int maxKBSize,boolean isRecycle)
    {
        return compress2Array(bitmap, Bitmap.CompressFormat.JPEG, 100, 0, 0, maxKBSize, isRecycle);
    }

    /**
     * 把Bitmap压缩成二进制数组
     *
     * @param bitmap
     * @param format       图片格式，传null默认为JPEG
     * @param quality      压缩品质，最大100
     * @param maxWidth     图片最大宽度，单位为像素
     * @param maxHeight    图片最大高度，单位为像素
     * @param maxKBSize    图片最大体积，单位为KB
     * @param isRecycleBmp 是否回收Bitmap
     * @return
     */
    public static byte[] compress2Array(Bitmap bitmap, Bitmap.CompressFormat format, int quality, float maxWidth, float maxHeight, int maxKBSize, boolean isRecycleBmp)
    {
        if (bitmap == null || quality < 1)
        {
            return null;
        }
        float widthScale = maxWidth > 0 ? maxWidth / bitmap.getWidth() : 1;
        float heightScale = maxHeight > 0 ? maxHeight / bitmap.getHeight() : 1;
        float scale = widthScale > heightScale ? heightScale : widthScale;
        if (scale < 1)
        {
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            bitmap = createBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), matrix, isRecycleBmp);
        }
        byte[] result = compress2Array(bitmap, format, quality, false);
        if (maxKBSize < 1 || result == null || result.length / 1024 <= maxKBSize)
        {
            if (isRecycleBmp && !bitmap.isRecycled())
            {
                bitmap.recycle();
            }
            return result;
        }
        for (int i = 0; i < 50; i++)
        {
            if (i % 4 == 3 && bitmap.getWidth() > 20 && bitmap.getHeight() > 20)
            {
                Matrix matrix = new Matrix();
                matrix.setScale(0.8f, 0.8f);
                bitmap = createBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), matrix, isRecycleBmp);
            } else
            {
                quality = quality <= 10 ? quality - 1 : (int) (quality * 0.8f);
                if (quality < 1)
                {
                    break;
                }
            }
            byte[] indexArray = compress2Array(bitmap, format, quality, false);
            if (indexArray == null)
            {
                break;
            }
            result = indexArray;
            if (result.length / 1024 <= maxKBSize)
            {
                break;
            }
        }
        if (isRecycleBmp && !bitmap.isRecycled())
        {
            bitmap.recycle();
        }
        return result;
    }

    /**
     * 把Bitmap压缩成二进制数组
     *
     * @param bitmap
     * @param format       图片格式，传null默认为JPEG
     * @param quality      压缩品质，最大100
     * @param isRecycleBmp 是否回收Bitmap
     * @return
     */
    public static byte[] compress2Array(Bitmap bitmap, Bitmap.CompressFormat format, int quality, boolean isRecycleBmp)
    {
        if (bitmap == null || quality < 1)
        {
            return null;
        }
        if (format == null)
        {
            format = Bitmap.CompressFormat.JPEG;
        }
        byte[] array;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try
        {
            bitmap.compress(format, quality, byteArrayOutputStream);
            byteArrayOutputStream.flush();
            array = byteArrayOutputStream.toByteArray();
        } catch (OutOfMemoryError e)
        {
            try
            {
                byteArrayOutputStream.reset();
                bitmap.compress(format, quality, byteArrayOutputStream);
                byteArrayOutputStream.flush();
                array = byteArrayOutputStream.toByteArray();
            } catch (Throwable e2)
            {
                array = null;
            }
        } catch (Exception e)
        {
            array = null;
        } finally
        {
            if (byteArrayOutputStream != null)
            {
                try
                {
                    byteArrayOutputStream.reset();
                    byteArrayOutputStream.close();
                } catch (IOException e)
                {
                }
                byteArrayOutputStream = null;
            }
            if (isRecycleBmp && !bitmap.isRecycled())
            {
                bitmap.recycle();
            }
        }
        return array;
    }
}
