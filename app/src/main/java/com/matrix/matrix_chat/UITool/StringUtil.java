package com.matrix.matrix_chat.UITool;

/**
 * @ClassName StringUtil
 * @Author Create By matrix
 * @Date 2023/5/19 0019 9:31
 */
public class StringUtil {

    /**
     * 检查字符串是否为空或等于空字符串("")
     * @param str 要检查的字符串
     * @return 如果字符串为空或等于空字符串，则返回true；否则返回false
     */
    public static boolean isEmptyOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
