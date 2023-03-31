package com.lxn.utilone.util.sign.rsa;

import com.lxn.utilone.R;
import com.lxn.utilone.UtilApplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @copyright:北京爱钱帮财富科技有限公司
 * 功能描述: 公钥私钥管理的工具类
 * 作 者:  李晓楠
 * 时 间： 2016/8/18 11:40
 */
public class KeyUtil {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    //公钥
    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5mkk84rFlRUO398acttMic8AA\n" +
            "EmDSZv5jUFaK/q/MXbbe33qmhqaZdclxpmmUwueGSMm1TyNzy1SHtzETVj8FjSiD\n" +
            "J33bxfEESvdOaRRu4rl51DLEE9tLQKw9lsB7vTLkIeJIafzdrixcth9JSzdke6dv\n" +
            "zycmTLNB0Td6iSIbhQIDAQAB";

    //私钥（和上面的并不是一套对应的）
    public static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM602wZNY9rWbRTr\n" +
            "TRjBOqkcdMUcGhH1FhyCjzhmqC/9Nqj6qIJx7+aty6VLrIaBCCh6hVTTZdDDy1pi\n" +
            "PHV9H7sNF4/01NpgY04cT+L64xcvW9hd6XxemghIBeOuKHCRtP5PTF9aRtFnMmy1\n" +
            "3E7XO+6qCX/Qw/YIzdo3rvrfbg/5AgMBAAECgYEAg0MDxwfD/kamNF+LsMmbx6ID\n" +
            "YNwMjhS1utsJucYuzXKdkEbEVTmnHzSEGm5om2060gGjw48Om7iJ3NZ/EBip2Ykx\n" +
            "AZvbJ2QS+hzGoKMAHf+xp0tY7Kj4kX2AjHDbc6j4u/MPK9VJrkyt/Dq3MoXtTTVV\n" +
            "oeOajgs/dvO7aDntALUCQQDpfo/U81iRK4pA2sg4KQLec1rwvjh76M8dGxS6ecgq\n" +
            "VsVvhT0Lhy6lNBDEMUBaLv4x1gIKKK15rB1G9hiHRiEvAkEA4qFOVu49InMB5RxI\n" +
            "bJ2qlo5BkVyyfkbKRV+1a94eSp1C8fRNzQUciJNG/pIxAk1Bv9QHBEiDZddGYm1o\n" +
            "VjeHVwJAUMzqSx/22y29BxpuxPh0NLSomLzK4R48Ze8UKj+wY+5pHmF2MYITQzoU\n" +
            "P0Bn641ti6NqS0euWHeWHhNVg6JiIwJAAemMiK0/9M91n6JKdigqbVTL9tcKZohg\n" +
            "Yo95L2zTYpvLGdGO6oD1ia18dt9y7GsMd/rpCD84ZJ6ZaHE15SxYxwJARxSnHxZq\n" +
            "4NuxMF63CchMhra+AhIvlqOd/RPU/N6WK8nNFEaX0R2/LoP2+ymSJl9+AgcyalPc\n" +
            "fpQmD+IM0i42Ng==";

    /**
     * 获取公钥key
     * @return RSAPublicKey
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey() throws Exception{
        return loadPublicKeyByStr(publicKey);
    }

    /**
     * 获取私钥key
     * @return  RSAPrivateKey
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey() throws Exception{
        return loadPrivateKeyByStr(privateKey);
    }
    /**
     * 把公钥字符串转换成一个公钥类
     * @param publicKeyStr  公钥字符串
     * @return
     * @throws Exception
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 把私钥字符串转换成一个私钥类
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }


    /**
     * 通用文件获取私钥
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyByFile() throws Exception {
        String privateStr = getPrivatekeystrByFile();
        return loadPrivateKeyByStr(privateStr);
    }
    /**
     * 通过raw文件夹下的文件获取 私钥字符串
     * @return
     * @throws Exception
     */
    private static String getPrivatekeystrByFile() throws Exception {
        InputStream in;
        //证书文件  pem一般是私钥
//        in = UtilApplication.getInstance().getResources().openRawResource(R.raw.app_ff_pkcs8);
        in = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String readLine;
            while ((readLine = br.readLine()) != null) {
                //去除 -----BEGIN PRIVATE KEY-----
                if (readLine.startsWith("--")) {
                    continue;
                } else {
                    sb.append(readLine);
                }
            }
            br.close();
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        } finally {
            if (null != in) in.close();
            if (null != br) br.close();
        }
        return sb.toString();
    }


    /**
     * 通过文件获取证书类
     * @return X509Certificate
     * @throws Exception
     */
    public static X509Certificate getCert() throws Exception{
        byte[] b;
        InputStream in;
        //  crt一般是公钥
        in = UtilApplication.getInstance().getResources().openRawResource(R.raw.ff_app_public);
        b = new byte[20480];
        in.read(b);
        if (null != in){
            in.close();
        }
        X509Certificate x509Certificate;
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        x509Certificate = (X509Certificate) cf.generateCertificate(bais);
        return x509Certificate;
    }
}
