package com.narcoding.actingaptitudetesting.Model;

/**
 * Created by Belgeler on 23.06.2017.
 */

public class Emoge {
    public String name;
    public String url;
    public String emotion;
    public Double percent;
    public int width;
    public int height;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getEmotion() {
        return emotion;
    }

    public int getWidth() {
        return width;
    }

    public Double getPercent() {
        return percent;
    }

    public int getHeight() {
        return height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
