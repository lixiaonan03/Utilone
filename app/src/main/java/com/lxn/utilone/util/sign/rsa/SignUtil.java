package com.lxn.utilone.util.sign.rsa;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;


/**
  *  @copyright:北京爱钱帮财富科技有限公司
  *  功能描述: RSA对数据加签 解签使用的工具类   私钥加签  公钥验签
  *   作 者:  李晓楠
  *   时 间： 2016/8/17 18:52 
 */
public class SignUtil {

	/**
	 * 签名算法  还有其他的 SHA1withRSA 等等
	 */
	public static final String SIGN_ALGORITHMS = "MD5WithRSA";


	/**
	 * 利用私钥对数据生成签名(ps:也就是私钥加签)
	 * @param plain  要加签的数据明文
	 * @return 经过base64编码的签名字符串
	 * @throws Exception
	 */
	public static String sign(String plain)throws Exception{
		if(null==plain){
			return "";
		}
		PrivateKey privateK = KeyUtil.getPrivateKey();
		//加签过程
		Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
		signature.initSign(privateK);
		signature.update(plain.getBytes());
		return Base64.encode(signature.sign());
	}
	/**
	 * 利用私钥对数据生成签名(ps:也就是私钥加签)
	 * @param data  数据的byte[]
	 * @param privateKey 私钥的字符串
	 * @return  经过base64编码的签名字符串
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {

		PrivateKey privateK = KeyUtil.loadPrivateKeyByStr(privateKey);
		//加签过程
		Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
		signature.initSign(privateK);
		signature.update(data);
		return Base64.encode(signature.sign());
	}

	/**
	 * 利用公钥对已生成的签名进行校验 （ps:也就是公钥验签）
	 * @param sign   已生成的签名值
	 * @param plain  数据明文的
	 * @param publicKey  公钥字符串
	 * @return   true表示签名成功 false表示签名校验失败
	 */
	public static boolean verify(String sign, String plain,String publicKey){
		/*X509Certificate x509Certificate;
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		x509Certificate = (X509Certificate) cf.generateCertificate(bais);
		java.security.cert.X509Certificate cert = PkCertFactory.getCert();*/
		boolean b = false;
		try {
			PublicKey pubKey =KeyUtil.loadPublicKeyByStr(publicKey);

			//因为签名算法是 MD5WithRSA  所以获取了数据明文的MD5值
			plain= MD5Util.getMd5(plain);
			byte signData[] = Base64.decode(sign);
			Signature sig = Signature.getInstance(SIGN_ALGORITHMS);
			//sig.initVerify(cert);
			sig.initVerify(pubKey);
			sig.update((null == plain ? "" : plain).getBytes("UTF-8"));
			b = sig.verify(signData);
		} catch (Exception e) {
			e.getMessage();
		}
		return b;
	}
	/**
	 * 利用公钥对已生成的签名进行校验 （ps:也就是公钥验签）
	 * @param sign   已生成的签名值
	 * @param plain  数据明文的
	 * @return   true表示签名成功 false表示签名校验失败
	 */
	public static boolean verify(String sign, String plain){
		boolean b = false;
		try {
			PublicKey pubKey = KeyUtil.getPublicKey();
			//X509Certificate pubKey = KeyUtil.getCert();

			//因为签名算法是 MD5WithRSA  所以获取了数据明文的MD5值
			plain= MD5Util.getMd5(plain);
			byte signData[] = Base64.decode(sign);
			Signature sig = Signature.getInstance(SIGN_ALGORITHMS);
			//sig.initVerify(cert);
			sig.initVerify(pubKey);
			sig.update((null == plain ? "" : plain).getBytes("UTF-8"));
			b = sig.verify(signData);
		} catch (Exception e) {
			e.getMessage();
		}
		return b;
	}
}
