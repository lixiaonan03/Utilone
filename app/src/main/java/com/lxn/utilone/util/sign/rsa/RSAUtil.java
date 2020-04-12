package com.lxn.utilone.util.sign.rsa;

import java.io.ByteArrayOutputStream;
import java.security.Key;

import javax.crypto.Cipher;

/**
  *  @copyright:北京爱钱帮财富科技有限公司
  *  功能描述: RAS非对称加密算法使用的工具类  使用非对称加密算法每次加密完的值是不同的
 *            公钥加密 私钥解密   后缀名为pem一般为私钥 对应使用的是 PKCS8EncodedKeySpec
 *            后缀名为crt的一般为公钥  对应使用的是 X509EncodedKeySpec
  *   作 者:  李晓楠
  *   时 间： 2016/6/29 15:41
 */
public class RSAUtil {
	  
    /**
     * 加密算法RSA 
     */  
    public static final String KEY_ALGORITHM = "RSA";  
    /**
     * RSA最大加密明文大小 
     */  
    private static final int MAX_ENCRYPT_BLOCK = 117;  
    /**
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 利用公钥对数据明文进行加密
     * @param str  数据明文
     * @param publicKey 公钥字符串
     * @return  经过base64 编码的加密完的字符串
     * @throws Exception
     */
    public static String encryptByPublicKey(String str,String publicKey) throws Exception{
        if (str==null ||str.length()<=0) return null;
        byte[] data = str.getBytes();
        Key publicK = KeyUtil.loadPublicKeyByStr(publicKey);
        //"RSA/ECB/PKCS1Padding"  “算法/模式/填充”或 “算法”
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encode(encryptedData);
    }
    /**
     * 利用公钥对数据明文进行加密
     * @param str  数据明文
     * @return  经过base64 编码的加密完的字符串
     * @throws Exception
     */
    public static String encryptByPublicKey(String str) throws Exception{
        if (str==null ||str.length()<=0) return null;
        byte[] data = str.getBytes();
        Key publicK = KeyUtil.getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //Cipher.ENCRYPT_MODE 将Cipher 初始化为加密模式的常量
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encode(encryptedData);
    }



    /**
     * 利用私钥对已加密的字符串进行解密
     * @param data  要解密的字符串
     * @param privateKey  私钥字符串
     * @return  解密出来的数据明文
     * @throws Exception
     */
    public static String  decryptByPrivateKey(String data, String privateKey) throws Exception {
        byte[] encryptedData=Base64.decode(data);

        Key privateK = KeyUtil.loadPrivateKeyByStr(privateKey);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }
    /**
     * 利用私钥对已加密的字符串进行解密
     * @param data  要解密的字符串
     * @return  解密出来的数据明文
     * @throws Exception
     */
    public static String  decryptByPrivateKey(String data) throws Exception {
        byte[] encryptedData=Base64.decode(data);

        Key privateK = KeyUtil.getPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //Cipher.DECRYPT_MODE Cipher 初始化为解密模式
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }
}