package com.rt12148.redditsample;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class SubredditContent extends RealmObject implements Serializable{

    private String likes;
    private String id;
    private String author;
    @Ignore
    private String selftext;
    @Ignore
    private Preview preview;
    private String thumbnail;
    private String subreddit;
    private int downs;
    private long created;
    private String title;
    private int num_comments;
    private int ups;

    public SubredditContent() {
    }

    public class Source {
        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public class Resolutions {
        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }


    public class Images {
        private Source source;
        private List<Resolutions> resolutions;
        private String id;

        public Source getSource() {
            return source;
        }

        public List<Resolutions> getResolutions() {
            return resolutions;
        }
    }

    public class Preview {
        private List<Images> images;
        private boolean enabled;

        public List<Images> getImages() {
            return images;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }

    String getTitle() {
        return title;
    }

    String getThumbnail() {
        return thumbnail;
    }

    int getDowns() {
        return downs;
    }

    int getUps() {
        return ups;
    }

    int getNumComments() {
        return num_comments;
    }

    Preview getPreview() {
        return preview;
    }

    String getSelfText() {
        return selftext;
    }

    void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }
}
