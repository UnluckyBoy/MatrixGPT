package com.matrix.matrix_chat.Network.ResponseBean.BackService;

import java.util.List;

/**
 * @ClassName WordsResult
 * @Author Create By matrix
 * @Date 2023/5/13 0013 21:46
 */
public class WordsResult {
    private List<WordObject> words_result;
    private int words_result_num;
    private long log_id;

    public List<WordObject> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordObject> words_result) {
        this.words_result = words_result;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }
}
