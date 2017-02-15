package com.cyy.ytextviewsimple;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyy.ytextview.Item;
import com.cyy.ytextview.Listener;
import com.cyy.ytextview.Text;
import com.cyy.ytextview.YTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by study on 17/2/13.
 *
 */

class MyAdapter extends BaseAdapter {
    ///评论回调
    public interface CommentCallback{
        void commentAction(View v , int position);
    }

    public List<Model> items;
    public Context context;

    private CommentCallback commentCallback;
    private MyViewClickListener mListener;

    public MyAdapter(Context context , List<Model> items){
        this.context = context ;
        this.items = items;
        mListener = new MyViewClickListener();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Model model = items.get(position);
        viewHolder.setModel(model);
        viewHolder.commentBtn.setTag(position);
        viewHolder.commentBtn.setOnClickListener(mListener);

        setCommentData(viewHolder.textView , model);
        return convertView;
    }

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    private void setCommentData(YTextView textView  , Model model){
        ///设置评论数据
        if (model.commentList!=null && model.commentList.size() > 0){
            List<Item> items = new ArrayList<>();
            for (Model.Comment comment: model.commentList) {
                Text.Builder builder = new Text.Builder()
                        .setCharSequence(buildSpannableString(comment))
                        .setOnItemTapListener(new Listener.OnItemTapListener() {
                            @Override
                            public void onItemTapListener(View view, int position) {
                                Toast.makeText(context, "position==="+position, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setLongPressedListener(new Listener.OnItemLongPressedListener() {
                            @Override
                            public boolean onItemLongPressedListener(View view, int position) {
                                Toast.makeText(context, "long press position==="+position, Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });
                items.add(builder.builder());
            }
            textView.setTexts(items);
        }else {
            textView.setTexts(Collections.<Item>emptyList());
        }
    }

    private SpannableString buildSpannableString(final Model.Comment comment){
        String commentItemString = comment.sender + "对"+comment.reciever +"说："+comment.message;
        SpannableString spannableString = new SpannableString(commentItemString);
        spannableString.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击代码
                Toast.makeText(context, "onClick"+comment.sender, Toast.LENGTH_SHORT).show();
            }

        }), 0, comment.sender.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(
                new ForegroundColorSpan(Color.RED), 0, comment.sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(
                new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击代码
                Toast.makeText(context, "onClick"+comment.reciever, Toast.LENGTH_SHORT).show();
                }

           }), comment.sender.length()+1,  comment.sender.length()+1 + comment.reciever.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(
                new ForegroundColorSpan(Color.RED), comment.sender.length()+1,  comment.sender.length()+1 + comment.reciever.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    class MyViewClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (commentCallback!=null)commentCallback.commentAction(v, (Integer) v.getTag());
        }
    }

    public void setCommentCallback(CommentCallback commentCallback) {
        this.commentCallback = commentCallback;
    }

    static class ViewHolder {
        protected ImageView headerView;
        protected TextView nameView;
        protected TextView message;
        protected ImageButton commentBtn;
        protected YTextView textView;

        ViewHolder(View rootView) {
            initView(rootView);
        }

        private void initView(View rootView) {
            headerView = (ImageView) rootView.findViewById(R.id.headerView);
            nameView = (TextView) rootView.findViewById(R.id.nameView);
            message = (TextView) rootView.findViewById(R.id.message);
            commentBtn = (ImageButton) rootView.findViewById(R.id.commentBtn);
            textView = (YTextView) rootView.findViewById(R.id.textView);
        }

        public void setModel(Model model){
            this.message.setText(model.message);
            this.nameView.setText(model.name);
        }
    }

}