package com.lxn.utilone.util.sign.des;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * @copyright:北京爱钱帮财富科技有限公司
 * 功能描述: DES加密对称算法使用的工具类  此加密算法使用同一秘钥加密完的值是相同的
 * 作 者:  李晓楠
 * 时 间： 2016/8/12 18:09
 */
public class DESUtil {
    //当前的加密算法为DES 对称加密算法
    private final static String DES = "DES";
    //对称加密的key
    public final static String Key = "qwe@1234";

    public static void main(String[] args) throws Exception {
        String data = "123 456";
        String key = "qwe@1234";
        System.out.println(encrypt(data, key));
        System.out.println(decrypt(encrypt(data, key), key));
    }

    /**
     * 对字符串进行加密
     * @param data  要加密的字符串
     * @param key   秘钥
     * @return   经过base64 编码的字符串
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        //TODO 这里可根据不同的情况选择不同的Base64 工具类
        // String strs = new BASE64Encoder().encode(bt);
         String strs =  Base64.encodeToString(bt, Base64.DEFAULT);
        return strs;
    }

    /**
     * 对byte数组进行加密
     * @param data  要加密的明文的byte数组
     * @param key  秘钥的byte数组
     * @return   加密完的byte数组
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
    /**
     * 对字符串进行解密
     * @param data  要解密的字符串
     * @param key   解密秘钥
     * @return   解密完的明文
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        if (data == null) return null;
        //TODO 这里可根据不同的情况选择不同的Base64 工具类
        byte[] buf =Base64.decode(data, Base64.DEFAULT);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt);
    }

    /**
     * 对byte数组进行解密
     * @param data 要解密的byte数组
     * @param key 解密秘钥
     * @return   解密完的byte数组
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
}
