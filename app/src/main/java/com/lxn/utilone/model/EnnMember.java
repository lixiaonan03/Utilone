package com.lxn.utilone.model;


public class EnnMember {
    
    /**
      * 会员
      **/
	private Integer membId;
    /**
      * 会员名称
      **/
	private String membName;
    /**
      * 01-个人
            02-企业
      **/
	private String memType;
    /**
      * 会员类型为02-企业时，该项不准为空
      **/
	private Integer companyId;
    /**
      * 会员等级ID
      **/
	private Integer levelId;
    /**
      * 
      **/
	private String membImg;
    /**
      * 01-自助邮件注册
            02-自助手机注册
            03-推送邮箱注册
            04-推送手机注册
            05-新侬卡注册
            06-第三方微信用户注册
            07-第三方QQ用户注册
      **/
	private String regType;
    /**
      * 01-待激活
            02-正常
            03-冻结
            04--注销
      **/
	private String membState;
    /**
      * 性别
      **/
	private String membSex;
    /**
      * 昵称
      **/
	private String membNick;
    /**
      * 
      **/
	private java.sql.Timestamp membBirth;
    /**
      * 
      **/
	private String province;
    /**
      * 
      **/
	private String city;
    /**
      * 
      **/
	private String country;
    /**
      * 
      **/
	private String membAdress;
    /**
      * 
      **/
	private String membPhone;
    /**
      * 
      **/
	private String membEmail;
    /**
      * 
      **/
	private String zip;
    /**
      * 
      **/
	private java.sql.Timestamp regTime;
    /**
      * 0-否
            1-是
      **/
	private String phoneIsvalid;
    /**
      * 
      **/
	private java.sql.Timestamp phoneVaildtime;
    /**
      * 
      **/
	private String emailIsvaild;
    /**
      * 
      **/
	private java.sql.Timestamp emailVaildtime;
    /**
      * 
      **/
	private String membUuid;
    /**
      * 
      **/
	private Integer membScore;
    /**
      * 
      **/
	private Integer membBeans;
    /**
      * 创建时间
      **/
	private java.sql.Timestamp creDate;
    /**
      * 修改时间
      **/
	private java.sql.Timestamp updDate;


	public EnnMember() {
		super();
	}

	public Integer getMembId() {
		return this.membId;
	}

	public void setMembId(Integer membId) {
		this.membId = membId;
	}
	public String getMembName() {
		return this.membName;
	}

	public void setMembName(String membName) {
		this.membName = membName;
	}
	public String getMemType() {
		return this.memType;
	}

	public void setMemType(String memType) {
		this.memType = memType;
	}
	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getLevelId() {
		return this.levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public String getMembImg() {
		return this.membImg;
	}

	public void setMembImg(String membImg) {
		this.membImg = membImg;
	}
	public String getRegType() {
		return this.regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}
	public String getMembState() {
		return this.membState;
	}

	public void setMembState(String membState) {
		this.membState = membState;
	}
	public String getMembSex() {
		return this.membSex;
	}

	public void setMembSex(String membSex) {
		this.membSex = membSex;
	}
	public String getMembNick() {
		return this.membNick;
	}

	public void setMembNick(String membNick) {
		this.membNick = membNick;
	}

	public java.sql.Timestamp getMembBirth() {
		return membBirth;
	}

	public void setMembBirth(java.sql.Timestamp membBirth) {
		this.membBirth = membBirth;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	public String getMembAdress() {
		return this.membAdress;
	}

	public void setMembAdress(String membAdress) {
		this.membAdress = membAdress;
	}
	public String getMembPhone() {
		return this.membPhone;
	}

	public void setMembPhone(String membPhone) {
		this.membPhone = membPhone;
	}
	public String getMembEmail() {
		return this.membEmail;
	}

	public void setMembEmail(String membEmail) {
		this.membEmail = membEmail;
	}
	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	public java.sql.Timestamp getRegTime() {
		return this.regTime;
	}

	public void setRegTime(java.sql.Timestamp regTime) {
		this.regTime = regTime;
	}
	public String getPhoneIsvalid() {
		return this.phoneIsvalid;
	}

	public void setPhoneIsvalid(String phoneIsvalid) {
		this.phoneIsvalid = phoneIsvalid;
	}
	public java.sql.Timestamp getPhoneVaildtime() {
		return this.phoneVaildtime;
	}

	public void setPhoneVaildtime(java.sql.Timestamp phoneVaildtime) {
		this.phoneVaildtime = phoneVaildtime;
	}
	public String getEmailIsvaild() {
		return this.emailIsvaild;
	}

	public void setEmailIsvaild(String emailIsvaild) {
		this.emailIsvaild = emailIsvaild;
	}
	public java.sql.Timestamp getEmailVaildtime() {
		return this.emailVaildtime;
	}

	public void setEmailVaildtime(java.sql.Timestamp emailVaildtime) {
		this.emailVaildtime = emailVaildtime;
	}
	public String getMembUuid() {
		return this.membUuid;
	}

	public void setMembUuid(String membUuid) {
		this.membUuid = membUuid;
	}
	public Integer getMembScore() {
		return this.membScore;
	}

	public void setMembScore(Integer membScore) {
		this.membScore = membScore;
	}
	public Integer getMembBeans() {
		return this.membBeans;
	}

	public void setMembBeans(Integer membBeans) {
		this.membBeans = membBeans;
	}
	public java.sql.Timestamp getCreDate() {
		return this.creDate;
	}

	public void setCreDate(java.sql.Timestamp creDate) {
		this.creDate = creDate;
	}
	public java.sql.Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(java.sql.Timestamp updDate) {
		this.updDate = updDate;
	}
}