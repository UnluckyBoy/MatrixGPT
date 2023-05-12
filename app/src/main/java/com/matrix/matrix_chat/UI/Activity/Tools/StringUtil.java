package com.matrix.matrix_chat.UI.Activity.Tools;

/**
 * @ClassName StringUtil
 * @Author Create By matrix
 * @Date 2023/5/12 0012 14:17
 */
public class StringUtil {
    public static boolean isNotEmpty(String msg){
        if(!(msg.equals(null))||msg!=null){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isEmpty(String msg){
        if(!(msg.equals(null))||msg!=null){
            return false;
        }else{
            return true;
        }
    }
}
