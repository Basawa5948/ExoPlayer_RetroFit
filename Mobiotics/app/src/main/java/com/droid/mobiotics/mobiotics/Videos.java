package com.droid.mobiotics.mobiotics;

public class Videos {

    private String description;
    private String id;
    private String thumb;
    private String title;
    private String url;

    public Videos(String description,String id,String thumb,String title,String url){
        this.description = description;
        this.id = id;
        this.thumb = thumb;
        this.title = title;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
