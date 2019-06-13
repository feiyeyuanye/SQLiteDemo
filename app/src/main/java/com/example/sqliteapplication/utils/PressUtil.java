package com.example.sqliteapplication.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.sqliteapplication.config.OrderContext;


/**
 * 处理按钮的按下效果
 */
public class PressUtil {
    /**
     * color.xml里面的id
     */
    public static int getColor(int colorResId){
        Context context = OrderContext.getInstance().getApplicationContext();
        return context.getResources().getColor(colorResId);
    }

    public static Drawable getDrawable(int drawableResId){
        Context context = OrderContext.getInstance().getApplicationContext();
        return context.getResources().getDrawable(drawableResId);
    }

    public static AutoBuildBackgroundDrawable getBgDrawable(Drawable drawable){
        return new AutoBuildBackgroundDrawable(drawable);
    }

    public static AutoBuildBackgroundDrawable getBgDrawable(int resId){
        return new AutoBuildBackgroundDrawable(getDrawable(resId));
    }
}
