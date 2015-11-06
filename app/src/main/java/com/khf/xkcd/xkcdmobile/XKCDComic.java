package com.khf.xkcd.xkcdmobile;

import android.graphics.Bitmap;

public class XKCDComic {
    private String titleName;
    private Bitmap bitmap;
    private String imageURL;
    private String currentURL;
    private String nextURL;
    private String prevURL;

    public XKCDComic() {}

    public XKCDComic(String title, Bitmap image) {
        this.titleName = title;
        this.bitmap = image;
    }

    public String getNextURL() {
        return nextURL;
    }

    public void setNextURL(String nextURL) {
        this.nextURL = nextURL;
    }

    public String getPrevURL() { return prevURL; }

    public void setPrevURL(String prevURL) { this.prevURL = prevURL; }

    public String getCurrentURL() { return currentURL; }

    public void setCurrentURL(String currentURL) { this.currentURL = currentURL; }

    public String getTitle() {
        return titleName;
    }

    public void setTitle(String title) {
        this.titleName = title;
    }

    public void setImage(Bitmap image) {
        this.bitmap = image;
    }

    public Bitmap getImage() {
        return bitmap;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
