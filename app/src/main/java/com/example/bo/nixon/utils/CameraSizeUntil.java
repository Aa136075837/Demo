package com.example.bo.nixon.utils;

import android.hardware.Camera.Size;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2016年4月23日 下午5:50:37
 * @说明 相机相片尺寸类
 */
public class CameraSizeUntil {

    /**
     * 获取预览的合适尺寸
     *
     * @param sizes
     * @param height
     * @param width
     * @return
     */
    public static Size getOptimalPreviewSize(List<Size> sizes, double height, double width) {
        final double ASPECT_TOLERANCE = 0.01;
        double targetRatio = height / width;
        if (sizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = (int) height;
        Collections.sort(sizes, new MyComparator());


        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.width - targetHeight) < minDiff) {
                optimalSize = size;
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) < minDiff || Math.abs(ratio - targetRatio) < 0.1) {
                    optimalSize = size;
                    minDiff = Math.abs(ratio - targetRatio);
                }
            }
        }


        return optimalSize;
    }


    /**
     * 获取相机拍照支持的尺寸
     *
     * @author Administrator
     */

    public static Size getOptimalTakePictrueSize(List<Size> sizes) {
        if (null == sizes)
            return null;

        Collections.sort(sizes, new MyComparator());
        Size size = null;
        for (int i = sizes.size() - 1; i >= 0; i--) {
            size = sizes.get(sizes.size() - 1);
            if (size.width <= 1920 && android.os.Build.BRAND.endsWith("sumsung")) //限制宽度1920 适配三星手机时避免保存图片时OOM
                break;
            else
                break;
        }
        if (null == size) {
            for (Size newSize : sizes) {
                size = newSize;
            }
        }
        return size;
    }

    private static class MyComparator implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // TODO Auto-generated method stub
            if (lhs.width > rhs.width)
                return 1;
            else if (lhs.width < rhs.width)
                return -1;
            else
                return 0;
        }
    }

    public static Size getMaxPictureSize(List<Size> sizes) {
        if (null == sizes)
            return null;
        Collections.sort(sizes, new MyComparator());
        Size size = null;
        for (int i = sizes.size() - 1; i >= 0; i--) {
            size = sizes.get(i);
            if (size.width <= 1920 && (android.os.Build.BRAND.endsWith("sumsung") ||
                    android.os.Build.BRAND.endsWith("samsung")))
                break;
            if (!(android.os.Build.BRAND.endsWith("sumsung") ||
                    android.os.Build.BRAND.endsWith("samsung")))
                break;
        }
        return size;
    }
}
