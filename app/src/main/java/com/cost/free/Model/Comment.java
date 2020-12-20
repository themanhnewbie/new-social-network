package com.cost.free.Model;

import java.util.HashMap;

public class Comment {
    private String commentId;
    private String userId;
    private String authorId;
    private String description;
    private String time;

    public Comment(String commentId, String userId, String authorId, String description, String time) {
        this.commentId = commentId;
        this.userId = userId;
        this.authorId = authorId;
        this.description = description;
        this.time = time;
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("commentId", commentId);
        map.put("userId", userId);
        map.put("authorId", authorId);
        map.put("description", description);
        map.put("time", time);
        return map;
    }

}
