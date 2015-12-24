package com.lxn.utilone.model;

import java.util.List;

public class GoodCarVO {
	private EnnGoods good;
	private String num;
	private String totalMoney;
	private List<EnnGoodsImg>  goodImgs;
	private int flag;//0 选中 1未选
    private String isElect;//0 实体 1电子
    
	public String getIsElect() {
		return isElect;
	}

	public void setIsElect(String isElect) {
		this.isElect = isElect;
	}

	public EnnGoods getGood() {
		return good;
	}

	public void setGood(EnnGoods good) {
		this.good = good;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public List<EnnGoodsImg> getGoodImgs() {
		return goodImgs;
	}

	public void setGoodImgs(List<EnnGoodsImg> goodImgs) {
		this.goodImgs = goodImgs;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
