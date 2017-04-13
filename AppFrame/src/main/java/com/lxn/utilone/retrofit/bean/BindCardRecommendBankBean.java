package com.lxn.utilone.retrofit.bean;

/**
  *  @copyright:北京爱钱帮财富科技有限公司
  *  功能描述: 绑卡是提示的推荐银行的bean
  *   作 者:  李晓楠
  *   时 间： 2016/9/2 16:45
 */
public class BindCardRecommendBankBean {
    private String bank_fullname;//银行名称
    private String single_limit;//单笔限额
    private String day_limit;//单日限额

    public String getDay_limit() {
        return day_limit;
    }

    public void setDay_limit(String day_limit) {
        this.day_limit = day_limit;
    }



    public String getBank_fullname() {
        return null==bank_fullname?"":bank_fullname;
    }

    public void setBank_fullname(String bank_fullname) {
        this.bank_fullname = bank_fullname;
    }

    public String getSingle_limit() {
        return single_limit;
    }

    public void setSingle_limit(String single_limit) {
        this.single_limit = single_limit;
    }
}
