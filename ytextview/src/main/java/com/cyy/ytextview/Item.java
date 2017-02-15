package com.cyy.ytextview;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;

/**
 * Created by cyy on 17/2/10.
 *
 */
 public class Item {

    int position;//索引位置

    Layout mLayout; ///文字的布局
    int height;//文字的总高度
    Rect rect;//文字的区域

    Listener.OnItemTapListener onItemTapListener;
    Listener.OnItemLongPressedListener onItemLongPressedListener;

    ///背景 暂时没用
    Drawable selectDrawable;///选择的背景

    public Item(Listener.OnItemTapListener tapListener ,
                Listener.OnItemLongPressedListener longPressedListener){
        this.onItemTapListener = tapListener;
        this.onItemLongPressedListener = longPressedListener;
    }

    Layout getmLayout() {
        return mLayout;
    }

    void setmLayout(Layout mLayout) {
        this.mLayout = mLayout;
        //计算文字的高度
        height = mLayout.getLineBottom(mLayout.getLineCount()-1);
    }

    int getHeight(){
        return height;
    }

    void setRect(Rect rect) {
        this.rect = rect;
    }

    public int getPosition(){
        return position;
    }

    public Listener.OnItemLongPressedListener getOnItemLongPressedListener() {
        return onItemLongPressedListener;
    }

    public void setOnItemLongPressedListener(Listener.OnItemLongPressedListener onItemLongPressedListener) {
        this.onItemLongPressedListener = onItemLongPressedListener;
    }

    public Listener.OnItemTapListener getOnItemTapListener() {
        return onItemTapListener;
    }

    public void setOnItemTapListener(Listener.OnItemTapListener onItemTapListener) {
        this.onItemTapListener = onItemTapListener;
    }
}
