package com.example.paul.swaplistview;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Paul on 30/3/17.
 */

public class SwapMenuItem {
    private int id;
    private Context context;
    private String title;
    private Drawable icon;
    private Drawable background;
    private int titleColor;
    private int titleSize;
    private int width;

    public SwapMenuItem(Context context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setIcon(int resId){
        this.icon = context.getResources().getDrawable(resId);
    }

    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public void setBackground(int resId){
        this.background = context.getResources().getDrawable(resId);
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
