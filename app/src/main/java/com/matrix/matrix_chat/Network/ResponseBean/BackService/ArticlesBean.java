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

    public class ArticleBean {
        private int mId;
        private String mTitle;
        private String mCover;
        private String mDescription;
        private String mContent;
        private String mAuthor;
        private int mHot;
        private String mType;
        private int mFiletype;

        public int getmId() {
            return mId;
        }

        public void setmId(int mId) {
            this.mId = mId;
        }

        public String getmTitle() {
            return mTitle;
        }

        public void setmTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public String getmCover() {
            return mCover;
        }

        public void setmCover(String mCover) {
            this.mCover = mCover;
        }

        public String getmDescription() {
            return mDescription;
        }

        public void setmDescription(String mDescription) {
            this.mDescription = mDescription;
        }

        public String getmContent() {
            return mContent;
        }

        public void setmContent(String mContent) {
            this.mContent = mContent;
        }

        public String getmAuthor() {
            return mAuthor;
        }

        public void setmAuthor(String mAuthor) {
            this.mAuthor = mAuthor;
        }

        public int getmHot() {
            return mHot;
        }

        public void setmHot(int mHot) {
            this.mHot = mHot;
        }

        public String getmType() {
            return mType;
        }

        public void setmType(String mType) {
            this.mType = mType;
        }

        public int getmFiletype() {
            return mFiletype;
        }

        public void setmFiletype(int mFiletype) {
            this.mFiletype = mFiletype;
        }

        @Override
        public String toString() {
            return "ArticleBean{" +
                    "mId=" + mId +
                    ", mTitle='" + mTitle + '\'' +
                    ", mCover='" + mCover + '\'' +
                    ", mDescription='" + mDescription + '\'' +
                    ", mContent='" + mContent + '\'' +
                    ", mAuthor='" + mAuthor + '\'' +
                    ", mHot=" + mHot +
                    ", mType='" + mType + '\'' +
                    ", mFiletype=" + mFiletype +
                    '}';
        }
    }
}
