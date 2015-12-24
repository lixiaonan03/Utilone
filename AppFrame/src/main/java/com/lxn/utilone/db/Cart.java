package com.lxn.utilone.db;

import java.io.Serializable;
import java.math.BigDecimal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 用户未登录情况下 购物车存储的商品类
 */
@DatabaseTable(tableName = "cart")
public class Cart implements Serializable{

	private static final long serialVersionUID = 1L;
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getGoodid() {
		return goodid;
	}
	public void setGoodid(int goodid) {
		this.goodid = goodid;
	}
	public String getGoodname() {
		return goodname;
	}
	public void setGoodname(String goodname) {
		this.goodname = goodname;
	}
	public String getGoodimgurl() {
		return goodimgurl;
	}
	public void setGoodimgurl(String goodimgurl) {
		this.goodimgurl = goodimgurl;
	}
	public BigDecimal getGoodprice() {
		return goodprice;
	}
	public void setGoodprice(BigDecimal goodprice) {
		this.goodprice = goodprice;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	public Cart() {
	}
    
	public int getIselectron() {
		return iselectron;
	}
	public void setIselectron(int iselectron) {
		this.iselectron = iselectron;
	}

	// 主键 id 自增长
	@DatabaseField(generatedId = true)
	public int _id;
	@DatabaseField(canBeNull=false)
	public int goodid;
	@DatabaseField(defaultValue="")
	public String goodname;
	@DatabaseField
	public String goodimgurl;
	@DatabaseField
	public BigDecimal goodprice;
	@DatabaseField
	public int num;
	/**
	 * 是否电子卡 0否 1是
	 */
	public int iselectron;
	private int flag;//0 选中 1未选

}
