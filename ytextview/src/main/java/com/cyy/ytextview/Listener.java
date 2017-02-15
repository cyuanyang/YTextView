package com.cyy.ytextview;

import android.view.View;

/**
 * Created by cyy on 17/2/11.
 *
 */

public interface Listener {

    interface OnItemTapListener {
        void onItemTapListener(View view , int position);
    }

    interface OnItemLongPressedListener{
        //返回true 不会出发单机事件 返回false 会执点击事件
        boolean onItemLongPressedListener(View view , int position);
    }
}
