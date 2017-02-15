package com.cyy.ytextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by study on 17/2/10.
 *
 */

public class YTextView extends View {

    private final static String TAG = "YTextView";

    private List<Item> items = new ArrayList<>();
    private TextPaint mTextPaint;
    private boolean isPressed;

    private int itemSpace = 0; //每一条之间的间距
    private float spacingmult = 1.0f; //
    private float spacingadd = 0.0f;

    private TouchListener touchListener;

    private Drawable selectDrawable;
    private int activePosition = -1;

    public YTextView(Context context) {
        super(context);
        init(null);
    }

    public YTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public YTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public YTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        int textColor = Color.BLACK;
        mTextPaint.setColor(textColor);
        setTextSize(15);

        if (attrs!=null){
            TypedArray a = this.getContext().obtainStyledAttributes(attrs , R.styleable.YTextView);
            int selectBackgroundInt = a.getResourceId(R.styleable.YTextView_y_selectBackground , 0 );
            if (selectBackgroundInt!=0){
                selectDrawable = this.getResources().getDrawable(selectBackgroundInt);
            }
            int textSize = a.getDimensionPixelSize(R.styleable.YTextView_y_textSize , 0);
            if (textSize!=0){
                setRawTextSize(textSize);
            }
            textColor = a.getColor(R.styleable.YTextView_y_textColor , textColor);
            if (mTextPaint.getColor()!=textColor){
                mTextPaint.setColor(textColor);
            }
            itemSpace = a.getDimensionPixelOffset(R.styleable.YTextView_y_itemSpace , 0);
            a.recycle();
        }

        touchListener = new TouchListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        touchListener.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //计算宽度 宽度只有充满 不支持包裹
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            width = widthSize;
        }

        ///计算每一个item的区域
        int lastItemBottom = obtainTextLayout(width);

        //计算高度
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            height = lastItemBottom == getPaddingTop() ? 0 : lastItemBottom + getPaddingBottom();
        }

        setMeasuredDimension(width , height);
    }

    ///计算每一个item的绘制区域 返回最后一个文本的bottom
    private int obtainTextLayout(int wantWidth){
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int originY = getPaddingTop();
        for (int i = 0 ; i<items.size() ; i++){
            Item item = items.get(i);
            item.position = i;
            if (item instanceof Text){
                Text text = (Text) item;
                if (text.text != null){
                    StaticLayout layout = new StaticLayout(
                            text.text, mTextPaint, wantWidth-paddingLeft-paddingRight,
                            Layout.Alignment.ALIGN_NORMAL, spacingmult, spacingadd, true);
                    text.setmLayout(layout);

                    ///计算每一个文字的rect
                    Rect rect = new Rect();
                    rect.top = originY;
                    rect.right = wantWidth-paddingRight;
                    rect.bottom = rect.top + text.getHeight() + itemSpace;
                    rect.left = paddingLeft;
                    text.setRect(rect);

                    originY = rect.bottom;
                }
            }
        }

        return originY;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int nextHeight = itemSpace/2 + getPaddingTop() ;///下一行的高度
        for (int i = 0; i<items.size() ; i++){
            Item item = items.get(i);
            if (item!=null){
                if (item instanceof Text){
                    Text text = (Text) item;

//                    Paint rectPaint = new Paint();
//                    rectPaint.setColor(Color.RED);
//                    rectPaint.setStyle(Paint.Style.STROKE);
//                    canvas.drawRect(text.rect , rectPaint);

                    if (i == activePosition && selectDrawable!=null){
                        selectDrawable.setBounds(text.rect);
                        selectDrawable.draw(canvas);
                    }

                    canvas.save();
                    canvas.translate(text.rect.left,nextHeight);
                    text.getmLayout().draw(canvas);
                    canvas.restore();//别忘了restore

                    nextHeight = text.rect.bottom + itemSpace/2;
                }
            }
        }
    }

    ///设置高亮的item的是哪一个  true 需要传入 position ｜ false 时position不需要
    void setActive(boolean b , int position ){
        this.isPressed = b;
        if (b){
            this.activePosition = position;
        }else {
            if (activePosition==-1){
                return;
            }
            this.activePosition = -1;
        }
        invalidate();
    }
    boolean getActive(){
        return this.isPressed;
    }

    /// 设置点击时的背景
    public void setSelectBackground(Drawable drawable){
        this.selectDrawable = drawable;
    }
    public void setmTextColor(int color){
        if (mTextPaint.getColor()!=color){
            mTextPaint.setColor(color);
            invalidate();
        }
    }

    public void setTexts(List<Item> item){
        setTexts(item , null);
    }
    public void setTexts(List<Item> item , Drawable selectDrable){
        this.items.clear();
        this.items.addAll(item);
        requestLayout();
    }

    public List<Item> getTexts(){
        return items;
    }

    /**
     * 设置字体的大小
     * @param size 单位是sp
     */
    public void setTextSize(int size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTextSize(int unit, float size) {
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        setRawTextSize(TypedValue.applyDimension(
                unit, size, r.getDisplayMetrics()));
    }
    private void setRawTextSize(float size) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);
            invalidate();
        }
    }

    public int getLineHeight(){
        return mTextPaint.getFontMetricsInt(null);
    }

    ///根据 xy 找到item
    private Item findItemByXY(int x , int y){
        for (Item item : this.getTexts()) {
            Rect rect = item.rect;
            if (rect.contains(x, y)){
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ///点击部分文字的实现
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

//            x -= widget.getTotalPaddingLeft();
//            y -= widget.getTotalPaddingTop();
//
//            x += widget.getScrollX();
//            y += widget.getScrollY();

            Item activeItem = findItemByXY(x , y);
            if (activeItem!=null){
                if (activeItem instanceof Text){
                    if (((Text) activeItem).text instanceof Spannable){
                        Spannable spannable = (Spannable) ((Text) activeItem).text;
                        Layout layout = activeItem.getmLayout();
                        int line = layout.getLineForVertical(y - activeItem.rect.top);
                        int off = layout.getOffsetForHorizontal(line, x);
                        Log.e("tag" , line+"<<<<<<<<<<<<line");
                        Log.e("tag" , off+"<<<<<<<<<<<<off");
                        ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);

                        if (link.length != 0) {
                            if (action == MotionEvent.ACTION_UP) {
                                link[0].onClick(this);
                            } else if (action == MotionEvent.ACTION_DOWN) {
                                Selection.setSelection( spannable,
                                        spannable.getSpanStart(link[0]),
                                        spannable.getSpanEnd(link[0]));
                            }
                            return true;
                        } else {
                            Selection.removeSelection(spannable);
                        }
                    }
                }
            }
        }
        return touchListener.onTouchEvent(event );
    }
}
