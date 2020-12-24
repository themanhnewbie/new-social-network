package com.cost.free.Model;

import java.util.HashMap;
import java.util.List;

public class Group {
    private String groupId, name, createdTime, adminId, image, cover;

    public Group() {
        super();
    }

    public Group(String groupId, String name, String image, String cover, String adminId) {
        this.groupId = groupId;
        this.name = name;
        this.adminId = adminId;
        this.cover = cover;
        this.image = image;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("adminId", adminId);
        map.put("groupId", groupId);
        map.put("name", name);
        map.put("image", image);
        map.put("cover", cover);
        return map;
    }
}
