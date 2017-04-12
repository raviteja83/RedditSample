package com.rt12148.redditsample;

import io.realm.RealmObject;

public class SubReddit extends RealmObject {

    private String subreddit;
    private boolean active;

    public SubReddit() {
    }

    public String getSubreddit() {
        return subreddit;
    }

    public boolean isActive() {
        return active;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
