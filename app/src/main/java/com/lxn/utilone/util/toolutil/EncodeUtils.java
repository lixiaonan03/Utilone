package com.lxn.utilone.util.toolutil;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author lixiaonan
 * 功能描述: 编码解码相关
 * 时 间： 2019-11-08 15:08
 */
public final class EncodeUtils {

    private EncodeUtils() {
    }

    /**
     * URL 编码
     * @param input
     * @return
     */
    public static String urlEncode(final String input) {
        return urlEncode(input, "UTF-8");
    }

    /**
     * URL 编码
     * @param input
     * @param charsetName
     * @return
     */
    public static String urlEncode(final String input, final String charsetName) {
        if (input == null || input.length() == 0) {
            return "";
        }
        try {
            return URLEncoder.encode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            //            throw new AssertionError(e);
            return "";
        }
    }

    /**
     * URL 编码
     * @param input
     * @return
     */
    public static String urlDecode(final String input) {
        return urlDecode(input, "UTF-8");
    }

    /**
     * URL 编码
     * @param input
     * @param charsetName
     * @return
     */
    public static String urlDecode(final String input, final String charsetName) {
        if (input == null || input.length() == 0) {
            return "";
        }
        try {
            return URLDecoder.decode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            //            throw new AssertionError(e);
            return "";
        }
    }

    /**
     * Base64 编码
     * @param input
     * @return
     */
    public static byte[] base64Encode(final String input) {
        return base64Encode(input.getBytes());
    }

    /**
     * Return Base64-encode bytes.
     *
     * @param input The input.
     * @return Base64-encode bytes
     */
    public static byte[] base64Encode(final byte[] input) {
        if (input == null || input.length == 0) {
            return new byte[0];
        }
        return Base64.encode(input, Base64.NO_WRAP);
    }

    /**
     * Return Base64-encode string.
     *
     * @param input The input.
     * @return Base64-encode string
     */
    public static String base64Encode2String(final byte[] input) {
        if (input == null || input.length == 0) {
            return "";
        }
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    /**
     * Base64 解码
     * @param input
     * @return
     */
    public static byte[] base64Decode(final String input) {
        if (input == null || input.length() == 0) {
            return new byte[0];
        }
        return Base64.decode(input, Base64.NO_WRAP);
    }

    /**
     * Return the bytes of decode Base64-encode bytes.
     *
     * @param input The input.
     * @return the bytes of decode Base64-encode bytes
     */
    public static byte[] base64Decode(final byte[] input) {
        if (input == null || input.length == 0) {
            return new byte[0];
        }
        return Base64.decode(input, Base64.NO_WRAP);
    }
}
