package com.matrix.matrix_chat.Network.ResponseBean.BackService;

import java.util.List;

/**
 * @ClassName ArticlesBean
 * @Author Create By matrix
 * @Date 2023/5/11 0011 19:50
 */
public class ArticlesBean {
    private List<ArticleBean> articles;

    public List<ArticleBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleBean> articles) {
        this.articles = articles;
    }
}
