package com.cost.free.Model;

import java.util.HashMap;

public class Comment {
    private String commentId;
    private String userId;
    private String description;
    private String time;
    private String postId;

    public Comment(){

    }

    public Comment(String commentId, String userId, String description, String time, String postId) {
        this.commentId = commentId;
        this.userId = userId;
        this.description = description;
        this.time = time;
        this.postId = postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("commentId", commentId);
        map.put("userId", userId);
        map.put("description", description);
        map.put("time", time);
        map.put("postId", postId);
        return map;
    }

}
