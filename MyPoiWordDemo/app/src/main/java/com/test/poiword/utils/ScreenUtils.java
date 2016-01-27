package com.test.poiword.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * ScreenUtils
 * <ul>
 * <strong>Convert between dp and sp</strong>
 * <li>{@link ScreenUtils#dpToPx(Context, float)}</li>
 * <li>{@link ScreenUtils#pxToDp(Context, float)}</li>
 * </ul>
 * 
 * @author fuweiwei 2015-9-2
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new AssertionError();
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context, float dp) {
        return (int)(dpToPx(context, dp) + 0.5f);
    }

    public static int pxToDpCeilInt(Context context, float px) {
        return (int)(pxToDp(context, px) + 0.5f);
    }
    /**
     *
     * @Title: getScreenWidth
     * @Description: TODO(获取屏幕宽度)
     */
    public static int getScreenWidth(Context context){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();

    }
    /**
     *
     * @Title: getScreenHeight
     * @Description: TODO(获取屏幕高度)
     */
    public static int getScreenHeight(Context context){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();

    }
}
