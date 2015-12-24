package com.lxn.utilone.model;


public class EnnGoodsImg {
    
    /**
      * 图片ID
      **/
	private Integer imgId;
    /**
      * 商品ID
      **/
	private Integer goodsId;
    /**
      * 图片路径
      **/
	private String imgPath;
    /**
      * 
      **/
	private Integer imgSort;
    /**
      * 创建人ID
      **/
	private Integer creUserId;
    /**
      * 创建人名称
      **/
	private String creUserName;
    /**
      * 创建人机构ID
      **/
	private Integer creOrgId;
    /**
      * 创建时间
      **/
	private java.sql.Timestamp creDate;
    /**
      * 修改人ID
      **/
	private Integer updUserId;
    /**
      * 修改人名称
      **/
	private String updUserName;
    /**
      * 修改人机构ID
      **/
	private Integer updOrgId;
    /**
      * 修改时间
      **/
	private java.sql.Timestamp updDate;
    /**
     * 01 主图     02 细节图
     */
	private String imgType;

	public EnnGoodsImg() {
		super();
	}

	public Integer getImgId() {
		return this.imgId;
	}

	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	public Integer getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public String getImgPath() {
		return this.imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Integer getImgSort() {
		return this.imgSort;
	}

	public void setImgSort(Integer imgSort) {
		this.imgSort = imgSort;
	}
	public Integer getCreUserId() {
		return this.creUserId;
	}

	public void setCreUserId(Integer creUserId) {
		this.creUserId = creUserId;
	}
	public String getCreUserName() {
		return this.creUserName;
	}

	public void setCreUserName(String creUserName) {
		this.creUserName = creUserName;
	}
	public Integer getCreOrgId() {
		return this.creOrgId;
	}

	public void setCreOrgId(Integer creOrgId) {
		this.creOrgId = creOrgId;
	}
	public java.sql.Timestamp getCreDate() {
		return this.creDate;
	}

	public void setCreDate(java.sql.Timestamp creDate) {
		this.creDate = creDate;
	}
	public Integer getUpdUserId() {
		return this.updUserId;
	}

	public void setUpdUserId(Integer updUserId) {
		this.updUserId = updUserId;
	}
	public String getUpdUserName() {
		return this.updUserName;
	}

	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	public Integer getUpdOrgId() {
		return this.updOrgId;
	}

	public void setUpdOrgId(Integer updOrgId) {
		this.updOrgId = updOrgId;
	}
	public java.sql.Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(java.sql.Timestamp updDate) {
		this.updDate = updDate;
	}


	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
}