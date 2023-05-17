package com.matrix.matrix_chat.Network.ResponseBean.BackService;

import java.io.Serializable;

/**
 * @ClassName UpArticleBean
 * @Author Create By matrix
 * @Date 2023/5/17 0017 16:27
 */
public class UpArticleBean implements Serializable {
    private String title;
    private String cover="default.png";
    private String description;
    private String content;
    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
