package com.matrix.matrixgpt.Network.CustomRequestModel;

/**
 * @ClassName CreImgRequestBody
 * @Author Create By Administrator
 * @Date 2023/4/8 0008 22:54
 */
public class CreImgRequestBody {
    private String prompt;//描述
    private int n=2;//要生成的图像数。必须介于 1 和 10 之间
    private String size="512x512";//生成的图像的大小。256x256,512x512,1024x1024
    private String response_format="url";//默认为 url,返回生成的图像的格式。必须url或b64_json

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getResponse_format() {
        return response_format;
    }

    public void setResponse_format(String response_format) {
        this.response_format = response_format;
    }
}
