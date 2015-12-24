package com.lxn.utilone.util;

/**
 * @Description:一些常量和访问服务器的一些路径配置
 */
public class CommonVariable {

    // 测试服务器地址
    public static String IP = "http://101.200.228.203:8181";
    //嵌套页地址
    public static String IP1="http://test.sun-yard.com";
    /**
     * 登录的url
     */
    public static String LoginURL = IP+ "/mallService/api/EnnUserLoginfo/getMemb/v2/";
    public static String HomeURL =IP1+ "/mall/new/home?device=android";
    /**
     * 商品详情url
     */
    public static String GoodDeatilURL = IP1+"/mall/cat/goodInfo?id=";
    /**
     * 获取短信验证码的接口
     */
    public static String GetCodeURL = IP
            + "/mallService/api/EnnSmsCode/getVlidateCode/v1/";
    /**
     * 找回用户密码的url
     * api/EnnUserLoginfo/getBackPass/v1/{phone}/{verifiCode}/{passwd}
     */
    public static String FindMemberPasswordURL = IP
            + "/mallService/api/EnnUserLoginfo/getBackPass/v1/";
    public static String GetGoodURL = IP
            + "/mallService/api/EnnGoods/searchByName/v3";
    /**
     * 获取商品分类的url 一级的带的参数为-100 2级的参数为 一级的id
     */
    public static String GetCatURL = IP
            + "/mallService/api/EnnGoodsCat/getCat/v1/";
    /**
     * 通过商品id获取商品详情
     */
    public static String GoodInfoURL = IP
            + "/mallService/api/EnnGoods/goodItem/v1/";
    /**
     * 更新用户会员信息 (头像信息)
     */
    public static String UpdateMemberinfoImgURL = IP
            + "/mallService/api/EnnMember/update/v1";
}
