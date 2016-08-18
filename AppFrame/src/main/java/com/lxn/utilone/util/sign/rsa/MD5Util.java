package com.lxn.utilone.util.sign.rsa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
  *  @copyright:北京爱钱帮财富科技有限公司
  *  功能描述: Md5 使用工具类
  *   作 者:  李晓楠
  *   时 间： 2016/8/18 14:26
 */
public class MD5Util {
    public static String getMd5(String str){
        try {
            return encript(str.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String encript(byte[] buffer) throws NoSuchAlgorithmException{
        String s  = null;
        char hexDigist[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(buffer);
        byte[] datas = md.digest(); //16个字节的长整数
        char[] str = new char[2*16];
        int k = 0;
        for(int i=0;i<16;i++){
            byte b   = datas[i];
            str[k++] = hexDigist[b>>>4 & 0xf];//高4位
            str[k++] = hexDigist[b & 0xf];//低4位
        }
        s = new String(str);
        return s;
    }
}
