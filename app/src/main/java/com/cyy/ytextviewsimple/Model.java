package com.cyy.ytextviewsimple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by study on 17/2/13.
 *
 */

public class Model {

    public String name;
    public String header;
    public String message;
    public List<Comment> commentList;

    public void setCommentList(Comment comment){
        if (commentList==null){
            commentList = new ArrayList<>();
        }
        commentList.add(comment);
    }

    public static class Comment{
        public String sender;
        public String reciever;
        public String message;
    }

}
