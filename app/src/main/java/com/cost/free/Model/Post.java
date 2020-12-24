package com.cost.free.Model;

import java.util.HashMap;

public class Post {
    String pId, pTitle, pDescr, pImage, uid, pLike, commentCount;


    public Post() {
    }

    public Post(String pId, String pTitle, String pDescr,
                String pImage, String uid, String pLike, String commentCount) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.pDescr = pDescr;
        this.pImage = pImage;
        this.uid = uid;
        this.pLike = pLike;
        this.commentCount = commentCount;
    }
    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getpLike() {
        return pLike;
    }

    public void setpLike(String pLike) {
        this.pLike = pLike;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescr() {
        return pDescr;
    }

    public void setpDescr(String pDescr) {
        this.pDescr = pDescr;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", this.uid);
        map.put("pId", this.pId);
        map.put("pTitle", this.pTitle);
        map.put("pDescr", this.pDescr);
        map.put("pImage", this.pImage);
        map.put("pLike", this.pLike);
        map.put("commentCount", this.commentCount);
        return map;
    }
}
