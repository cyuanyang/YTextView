package com.cyy.ytextviewsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , MyAdapter.CommentCallback , CommentHelper.Callback{

    protected ListView listView;
    protected TextView textView;
    private MyAdapter adapter;

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
        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void commentAction(View v, int position) {
        Log.i("TAG" , "position = " + position);
        commentHelper.comment(position);
    }


    @Override
    public void onClick(View view) {
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
