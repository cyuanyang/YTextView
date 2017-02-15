package com.cyy.ytextviewsimple;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyy.ytextview.Item;
import com.cyy.ytextview.Listener;
import com.cyy.ytextview.Text;
import com.cyy.ytextview.YTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , MyAdapter.CommentCallback , CommentHelper.Callback{

    protected ListView listView;
//    protected EditText editText;
//    protected Button completeBtn;
    protected TextView textView;
    private MyAdapter adapter;

    private List<List<Item>> items = new ArrayList<>();
    private List<Model> datas = new ArrayList<>();

    private CommentHelper commentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        commentHelper = new CommentHelper(this);
        commentHelper.setmCallback(this);
        initView();
        adapter = new MyAdapter(this , datas);
        listView.setAdapter(adapter);
        adapter.setCommentCallback(this);
        initData();
    }

    private void initData() {
        for (int i = 0 ; i < 100 ; i++){
            Model mo = new Model();
            mo.message = "尼奥终于意识到自己的能力和使命，在中弹“复活”的同时，他也变成了无所不能“救世主”。" +
                    "结尾的“飞升”象征着人类超级英雄的诞生：尼奥将带领锡安基地的人民，打响对机器世界的反击战，" +
                    "并将以胜利者的姿态结束这场战斗，还人类以自由之身。他的使命会实现吗？母体会那么甘心被摧毁吗？";
            mo.name = "史密斯";
            datas.add(mo);
        }
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
//        editText = (EditText) findViewById(R.id.editText);
//        completeBtn = (Button) findViewById(R.id.completeBtn);
//        completeBtn.setOnClickListener(MainActivity.this);
        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void commentAction(View v, int position) {
        Log.i("TAG" , "position = " + position);
        commentHelper.comment(position);
    }

//    class Clickable extends ClickableSpan implements View.OnClickListener {
//        private final View.OnClickListener mListener;
//
//        public Clickable(View.OnClickListener l) {
//            mListener = l;
//        }
//
//        @Override
//        public void onClick(View v) {
//            mListener.onClick(v);
//        }
//    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.completeBtn) {
//            Text text = new Text();
//            SpannableString spannableString = new SpannableString(editText.getText().toString());
//            spannableString.setSpan(new Clickable(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //点击代码
//                    Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_SHORT).show();
//                }
//
//            }), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            text.text = spannableString;
//            text.setOnItemTapListener(new Listener.OnItemTapListener() {
//                @Override
//                public void onItemTapListener(View view, int position) {
//                    Toast.makeText(MainActivity.this, MainActivity.this.items.get(0).get(position).getPosition() + "", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            if (items.size() == 0) {
//                List<Item> item = new ArrayList<>();
//                item.add(text);
//                items.add(item);
//            } else {
//                items.get(0).add(text);
//            }
//            adapter.notifyDataSetChanged();
//        }
    }


    @Override
    public void commentComplete(String msg, int position) {
        Model model = datas.get(position);
        Model.Comment comment = new Model.Comment();
        comment.sender = "尼奥";
        comment.reciever = "史密斯";
        comment.message = msg;
        model.setCommentList(comment);

        adapter.notifyDataSetChanged();
    }
}
