package com.cyy.ytextviewsimple;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by study on 17/2/13.
 *
 */

public class CommentHelper {

    public interface Callback{
        void commentComplete(String msg , int position);
    }

    private MainActivity activity;

    private EditText editText;
    private RelativeLayout commentLayout;
    private Button commentBtn;

    private int position;

    private Callback mCallback;

    public CommentHelper(MainActivity activity){
        this.activity = activity;

        editText = (EditText) activity.findViewById(R.id.commentEditView);
        commentLayout = (RelativeLayout) activity.findViewById(R.id.commentLayout);
        commentBtn = (Button) activity.findViewById(R.id.comment_send);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback!=null && !TextUtils.isEmpty(editText.getText())){
                    mCallback.commentComplete(editText.getText().toString() , position);
                }
            }
        });
    }

    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public void comment(int position){
        this.position = position;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        this.commentLayout.setVisibility(View.VISIBLE);
        this.editText.requestFocus();
    }

    public void cancelComment(){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        this.commentLayout.setVisibility(View.GONE);
    }
}
