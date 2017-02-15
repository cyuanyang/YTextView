package com.cyy.ytextview;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by study on 17/2/11.
 *
 */

class TouchListener {

    private YTextView textView;
    private CheckForLongPress mCheckForLongPress;
    private CheckForTap mCheckFortap;

    private boolean mHasPerformLongClick; //是否执行了长按事件

    TouchListener(YTextView textView){
        this.textView = textView;
    }

    private int downX = 0, downY = 0;
    private  Item activeItem;

    public void onWindowFocusChanged(boolean hasWindowFocus){
        if (!hasWindowFocus){
            if (textView.getActive()){
                textView.setActive(false , -1);
            }
            removeTapCallback();
            removeLongTapCallBack();
        }
    }

    ///事件处理
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:

                mHasPerformLongClick = false;
                removeLongTapCallBack();

                downX = (int) event.getX();
                downY = (int) event.getY();

                ///找到点击的是哪一个item
                activeItem = findItemByXY(downX , downY);
                if (activeItem!=null){
                    if (isInScrollingContainer(textView)){
                        if (mCheckFortap == null)mCheckFortap = new CheckForTap();
                        mCheckFortap.activeItem = activeItem;
                        mCheckFortap.yTextView = textView;
                        textView.postDelayed(mCheckFortap , ViewConfiguration.getTapTimeout()/2);
                    }else {
                        textView.setActive(true , activeItem.position);
                        checkLongPress(activeItem);
                    }
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (activeItem!=null){
                    textView.setActive(false , activeItem.position);
                    activeItem = null;
                }
                removeTapCallback();
                removeLongTapCallBack();
                break;

            case MotionEvent.ACTION_UP:
                if (activeItem!=null){
                    int deltaX = Math.abs(downX - (int)event.getX());
                    int deltaY = Math.abs(downY - (int)event.getY());

                    if (!mHasPerformLongClick
                            && Math.max(deltaX , deltaY) < ViewConfiguration.getWindowTouchSlop()){
                        Log.i("tag" , activeItem.rect+"");
                        if (activeItem.onItemTapListener !=null){
                            activeItem.onItemTapListener.onItemTapListener(textView , activeItem.position);
                        }
                    }
                    textView.setActive(false , activeItem.position);
                    activeItem = null;
                }
                removeTapCallback();
                removeLongTapCallBack();
                break;

            case MotionEvent.ACTION_CANCEL:
                if (activeItem!=null){
                    textView.setActive(false , activeItem.position);
                    activeItem = null;
                }
                removeTapCallback();
                removeLongTapCallBack();
                break;
        }
        return true;
    }

    public boolean isInScrollingContainer(View view) {
        ViewParent p = view.getParent();
        while (p != null && p instanceof ViewGroup) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    ///检测是否长按事件
    private void checkLongPress(Item activeItem){
        if (activeItem.getOnItemLongPressedListener()==null){
            return;
        }
        if (mCheckForLongPress==null)mCheckForLongPress = new CheckForLongPress();
        mCheckForLongPress.setOnItemLongPressedListener(activeItem.onItemLongPressedListener);
        mCheckForLongPress.setPosition(activeItem.position);
        textView.postDelayed(mCheckForLongPress , ViewConfiguration.getLongPressTimeout()-ViewConfiguration.getTapTimeout());
    }

    ///根据 xy 找到item
    private Item findItemByXY(int x , int y){
        for (Item item : textView.getTexts()) {
            Rect rect = item.rect;
            if (rect.contains(x, y)){
                return item;
            }
        }
        return null;
    }

    private void removeTapCallback(){
        if (mCheckFortap!=null){
            textView.removeCallbacks(mCheckFortap);
        }
    }

    private void removeLongTapCallBack(){
        if (mCheckForLongPress!=null){
            textView.removeCallbacks(mCheckForLongPress);
        }
    }

    class CheckForTap implements Runnable{
        YTextView yTextView;
        Item activeItem;
        @Override
        public void run() {
            if (yTextView!=null&&activeItem!=null){
                yTextView.setActive(true , activeItem.position);
                checkLongPress(activeItem);
            }
        }
    }

    class CheckForLongPress implements Runnable{

        private Listener.OnItemLongPressedListener onItemLongPressedListener;
        private int position;

        @Override
        public void run() {
            if (onItemLongPressedListener!=null &&
                    textView.getActive() && onItemLongPressedListener.onItemLongPressedListener(textView , position)){
                mHasPerformLongClick = true;
            }
        }

        void setOnItemLongPressedListener(Listener.OnItemLongPressedListener onItemLongPressedListener) {
            this.onItemLongPressedListener = onItemLongPressedListener;
        }

        void setPosition(int position) {
            this.position = position;
        }
    }
}
