package com.matrix.matrixgpt.Network.ResponseBean.Gpt;

import java.util.List;

/**
 * @ClassName CreateImageBean
 * @Author Create By Administrator
 * @Date 2023/4/8 0008 23:03
 */
public class CreateImageBean {
    private int created;
    private List<ResponseData> data;

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public List<ResponseData> getData() {
        return data;
    }

    public void setData(List<ResponseData> data) {
        this.data = data;
    }

    public class ResponseData {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
