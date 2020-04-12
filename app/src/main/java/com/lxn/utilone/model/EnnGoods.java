package com.lxn.utilone.model;

import java.math.BigDecimal;


public class EnnGoods {
    
    /**
      * 商品ID
      **/
	private Integer goodsId;
    /**
      * 分类ID
      **/
	private Integer catId;
    /**
      * 品牌ID
      **/
	private Integer brandId;
    /**
      * 
      **/
	private String goodsCode;
    /**
      * 
      **/
	private String goodsBarcode;
    /**
      * 商品名称
      **/
	private String goodsName;
    /**
      * 
      **/
	private String goodsTname;
    /**
      * 规格型号
      **/
	private String goodsSpec;
    /**
      * 0-停产
            1-正常
      **/
	private String produceState;
    /**
      * 
      **/
	private Integer supplierId;
    /**
      * 
      **/
	private String supplierName;
    /**
      * 01-待发布
            02-上架
            03-下架
      **/
	private String goodsState;
    /**
      * 参考价格
      **/
	private Double goodsPrice;
    /**
      * 
      **/
	private String goodsProduce;
    /**
      * 
      **/
	private String facName;
    /**
      * 
      **/
	private String facAddress;
    /**
      * 0-否
            1-是
      **/
	private Integer isDelivery;
    /**
      * 01-实物 02-会员卡 03-礼品卡
      **/
	private String goodsType;
    /**
      * 
      **/
	private String goodsWeight;
    /**
      * 
      **/
	private String goodUnit;
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
      * 商品总评价分
      **/
	private BigDecimal goodsEvals;
    /**
      * 商品总销量分
      **/
	private BigDecimal goodsSales;
    /**
      * 
      **/
	private java.sql.Timestamp onshelfTime;
    /**
      * 
      **/
	private java.sql.Timestamp offshelfTime;
    /**
      * 商品总库存
      **/
	private Integer goodsStock;
    /**
      * 是否菜箱：
            0-否
            1-是
      **/
	private String isBox;
    /**
      * 包装[长]- 单位：CM
      **/
	private BigDecimal goodsLength;
    /**
      * 包装[宽]- 单位：CM
      **/
	private BigDecimal goodsWidth;
    /**
      * 包装[高]- 单位：CM
      **/
	private BigDecimal goodsHigh;
    /**
      * 会员卡类型ID:具体关联某一种会员卡：
            8斤8种 蔬菜卡 年卡 52次 每周一次 
      **/
	private Integer goodsIc;
    /**
      * 商城优惠价
      **/
	private BigDecimal mallPrice;
    /**
      * 商品促销价
      **/
	private BigDecimal promPrice;

	  /**
     * 0-否
           1-是
     **/
	private Integer isdianzi;
	
	public Integer getIsdianzi() {
		return isdianzi;
	}

	public void setIsdianzi(Integer isdianzi) {
		this.isdianzi = isdianzi;
	}

	public EnnGoods() {
		super();
	}

	public Integer getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getCatId() {
		return this.catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	public Integer getBrandId() {
		return this.brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getGoodsCode() {
		return this.goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsBarcode() {
		return this.goodsBarcode;
	}

	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}
	public String getGoodsName() {
		return this.goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsTname() {
		return this.goodsTname;
	}

	public void setGoodsTname(String goodsTname) {
		this.goodsTname = goodsTname;
	}
	public String getGoodsSpec() {
		return this.goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	public String getProduceState() {
		return this.produceState;
	}

	public void setProduceState(String produceState) {
		this.produceState = produceState;
	}
	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getGoodsState() {
		return this.goodsState;
	}

	public void setGoodsState(String goodsState) {
		this.goodsState = goodsState;
	}
	public Double getGoodsPrice() {
		return this.goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getGoodsProduce() {
		return this.goodsProduce;
	}

	public void setGoodsProduce(String goodsProduce) {
		this.goodsProduce = goodsProduce;
	}
	public String getFacName() {
		return this.facName;
	}

	public void setFacName(String facName) {
		this.facName = facName;
	}
	public String getFacAddress() {
		return this.facAddress;
	}

	public void setFacAddress(String facAddress) {
		this.facAddress = facAddress;
	}
	public Integer getIsDelivery() {
		return this.isDelivery;
	}

	public void setIsDelivery(Integer isDelivery) {
		this.isDelivery = isDelivery;
	}
	public String getGoodsType() {
		return this.goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getGoodsWeight() {
		return this.goodsWeight;
	}

	public void setGoodsWeight(String goodsWeight) {
		this.goodsWeight = goodsWeight;
	}
	public String getGoodUnit() {
		return this.goodUnit;
	}

	public void setGoodUnit(String goodUnit) {
		this.goodUnit = goodUnit;
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
	public BigDecimal getGoodsEvals() {
		if(null==this.goodsEvals){
			this.goodsEvals=new BigDecimal(0);
		}
		return this.goodsEvals;
	}

	public void setGoodsEvals(BigDecimal goodsEvals) {
		this.goodsEvals = goodsEvals;
	}
	public BigDecimal getGoodsSales() {
		if(null==this.goodsSales){
			this.goodsSales=new BigDecimal(0);
		}
		return this.goodsSales;
	}

	public void setGoodsSales(BigDecimal goodsSales) {
		this.goodsSales = goodsSales;
	}
	public java.sql.Timestamp getOnshelfTime() {
		return this.onshelfTime;
	}

	public void setOnshelfTime(java.sql.Timestamp onshelfTime) {
		this.onshelfTime = onshelfTime;
	}
	public java.sql.Timestamp getOffshelfTime() {
		return this.offshelfTime;
	}

	public void setOffshelfTime(java.sql.Timestamp offshelfTime) {
		this.offshelfTime = offshelfTime;
	}
	public Integer getGoodsStock() {
		return this.goodsStock;
	}

	public void setGoodsStock(Integer goodsStock) {
		this.goodsStock = goodsStock;
	}
	public String getIsBox() {
		return this.isBox;
	}

	public void setIsBox(String isBox) {
		this.isBox = isBox;
	}
	public BigDecimal getGoodsLength() {
		return this.goodsLength;
	}

	public void setGoodsLength(BigDecimal goodsLength) {
		this.goodsLength = goodsLength;
	}
	public BigDecimal getGoodsWidth() {
		return this.goodsWidth;
	}

	public void setGoodsWidth(BigDecimal goodsWidth) {
		this.goodsWidth = goodsWidth;
	}
	public BigDecimal getGoodsHigh() {
		return this.goodsHigh;
	}

	public void setGoodsHigh(BigDecimal goodsHigh) {
		this.goodsHigh = goodsHigh;
	}
	public Integer getGoodsIc() {
		return this.goodsIc;
	}

	public void setGoodsIc(Integer goodsIc) {
		this.goodsIc = goodsIc;
	}
	public BigDecimal getMallPrice() {
		return this.mallPrice;
	}

	public void setMallPrice(BigDecimal mallPrice) {
		this.mallPrice = mallPrice;
	}
	public BigDecimal getPromPrice() {
		return this.promPrice;
	}

	public void setPromPrice(BigDecimal promPrice) {
		this.promPrice = promPrice;
	}
}