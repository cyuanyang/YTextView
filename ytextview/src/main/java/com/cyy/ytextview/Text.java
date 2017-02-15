package com.cyy.ytextview;


/**
 * Created by cyy on 17/2/10.
 * 纯文本的 item
 */
public class Text extends Item {

    public CharSequence text;

    public Text(CharSequence cs){
        this(cs , null , null);
    }

    public Text(CharSequence cs ,
                Listener.OnItemTapListener tapListener ,
                Listener.OnItemLongPressedListener longPressedListener){
        super(tapListener , longPressedListener);
        this.text = cs;
    }


    public static class Builder{
        private CharSequence charSequence;
        private Listener.OnItemLongPressedListener longPressedListener;
        private Listener.OnItemTapListener onItemTapListener;

        public Builder setCharSequence(CharSequence cs){
            charSequence = cs;
            return this;
        }

        public Builder setLongPressedListener(Listener.OnItemLongPressedListener longPressedListener) {
            this.longPressedListener = longPressedListener;
            return this;
        }

        public Builder setOnItemTapListener(Listener.OnItemTapListener onItemTapListener) {
            this.onItemTapListener = onItemTapListener;
            return this;
        }

        public Text builder(){
            return new Text(charSequence , onItemTapListener , longPressedListener);
        }
    }
}
