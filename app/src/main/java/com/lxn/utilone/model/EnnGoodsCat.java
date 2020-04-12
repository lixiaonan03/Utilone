package com.lxn.utilone.model;

/**
 * 解析的实体类
 */
public class EnnGoodsCat {
    
    /**
      * 分类ID
      **/
	private Integer catId;
    /**
      * 分类名称
      **/
	private String catName;
    /**
      * 父类ID
      **/
	private Integer parentId;
    /**
      * 0-否 1-是
      **/
	private Integer isEnable;
    /**
      * 商品分类级次
      **/
	private Integer catLevel;
    /**
      * 
      **/
	private Integer catSort;
    /**
      * pic1|uploadimage
      **/
	private String catImg;
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


	public EnnGoodsCat() {
		super();
	}

	public Integer getCatId() {
		return this.catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return this.catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	public Integer getCatLevel() {
		return this.catLevel;
	}

	public void setCatLevel(Integer catLevel) {
		this.catLevel = catLevel;
	}
	public Integer getCatSort() {
		return this.catSort;
	}

	public void setCatSort(Integer catSort) {
		this.catSort = catSort;
	}
	public String getCatImg() {
		return this.catImg;
	}

	public void setCatImg(String catImg) {
		this.catImg = catImg;
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

}