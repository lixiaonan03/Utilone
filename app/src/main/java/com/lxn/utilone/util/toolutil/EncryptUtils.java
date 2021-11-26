package com.lxn.utilone.util.toolutil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lixiaonan
 * 功能描述: 加解密相关的工具类
 * 时 间： 2020/7/13 5:05 PM
 */
public final class EncryptUtils {

    private EncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    ///////////////////////////////////////////////////////////////////////////
    // hash encryption
    ///////////////////////////////////////////////////////////////////////////


    /**
     * MD5 加密
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMD5ToString(final String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptMD5ToString(data.getBytes());
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMD5ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptMD5(data));
    }

    /**
     * md5加密的
     *
     * @param data The data.
     * @return the bytes of MD5 encryption
     */
    public static byte[] encryptMD5(final byte[] data) {
        return hashTemplate(data, "MD5");
    }

    /**
     * 返回哈希的字节码
     *
     * @param data      The data.
     * @param algorithm The name of hash encryption.
     * @return the bytes of hash encryption
     */
    public static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
