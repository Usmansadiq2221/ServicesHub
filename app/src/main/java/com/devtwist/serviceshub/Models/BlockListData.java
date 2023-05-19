package com.devtwist.serviceshub.Models;

public class BlockListData {
    private String userId;

    public BlockListData() {
    }

    public BlockListData(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
