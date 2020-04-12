package com.lxn.utilone.util.sign.aes;

import android.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by li on 2016/8/12 15:50.
 * AES加密对称算法使用的工具类  此加密算法使用同一秘钥加密完的值是相同的
 */
public class AESUtil {

    public static void main(String[] args) throws Exception {
        String content = "我爱你";
        String key = "123456";

        String encrypt = aesEncrypt(content, key);
        System.out.println("加密后：" + encrypt);
        String decrypt = aesDecrypt(encrypt, key);
        System.out.println("解密后：" + decrypt);
    }
    /**
     * AES加密明文
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后经过base64 编码的字符串
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }
    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(encryptKey.getBytes()));
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        return cipher.doFinal(content.getBytes("utf-8"));
    }
    /**
     * 对字节数组进行base64 编码    可以根据不同的情况选择不同base64 工具类
     * @param bytes 待编码的byte[]
     * @return 经过编码后的字符串
     */
    private static String base64Encode(byte[] bytes){
        //TODO 可以根据不同的情况选择不同base64 工具类
        // return new BASE64Encoder().encode(bytes);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }



    /**
     * AES解密
     * @param encryptStr 待解密的字符串
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return encryptStr==null? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }
    /**
     * AES解密
     * @param encryptBytes 待解密字符串的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(decryptKey.getBytes()));

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }
    /**
     * 对String字符串进行base64解码   可以根据不同的情况选择不同base64 工具类
     * @param base64Code 待解码的字符串
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        //TODO  可以根据不同的情况选择不同base64 工具类
        // return base64Code==null ? null : new BASE64Decoder().decodeBuffer(base64Code);
        return base64Code==null ? null : Base64.decode(base64Code, Base64.DEFAULT);
    }





    /**
     * 获取一个字符串的md5值(ps:对一个字符串进行MD5加密)
     * @param msg  要加密的字符串
     * @return md5  加密完的byte[]值
     * @throws Exception
     */
    public static byte[] md5(String msg) throws Exception {
        return msg==null ? null : md5(msg.getBytes());
    }
    /**
     * 获取byte[]的md5值
     * @param bytes byte[]
     * @return md5 加密完的byte[]值
     * @throws Exception
     */
    public static byte[] md5(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5Util");
        md.update(bytes);
        return md.digest();
    }
    /**
     * 对一个字符串进行MD5加密
     * @param msg 待加密字符串
     * @return 返回经过base64 编码的字符串
     * @throws Exception
     */
    public static String md5Encrypt(String msg) throws Exception{
        return msg==null ? null : base64Encode(md5(msg));
    }


    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }
}
