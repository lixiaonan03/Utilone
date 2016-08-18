package com.lxn.utilone.util.sign.rsa;


import com.lxn.utilone.util.sign.rsa.RSAUtil;
import com.lxn.utilone.util.sign.rsa.SignUtil;

public class Test {

	public static void main(String[] args) throws Exception {
		String aaa="device_id=C6CF9560-DF86-47DF-88F3-7EE3D1D25983&password=123abc&phone=13260131837";
		String sign = SignUtil.sign(aaa);
		System.out.println("-------加签 验签---------------");
		System.out.println("加签结果==="+sign);
		boolean b = SignUtil.verify(sign, aaa);
		//因为现在key 不是对应的所以 验签会是false
		System.out.println("验签结果====="+b);


		System.out.println("-------加密 ---------------");
		String str = "123456";
		String  encrypt = RSAUtil.encryptByPublicKey(str);
		System.out.println("加密后："+encrypt);
		
	}

}
