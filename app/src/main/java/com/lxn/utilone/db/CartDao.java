package com.lxn.utilone.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.lxn.utilone.UtilApplication;

/**
 * 购物车的操作方法
 * @author lxn
 *
 */
public class CartDao {
	private Context context;
	private Dao<Cart, Integer> userDao = null;
	private DataBaseHelper helper = DataBaseHelper.getHelper(UtilApplication
			.getInstance());
	private static CartDao instance;

	private CartDao() {
		try {
			userDao = helper.getDao(Cart.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static CartDao getInstance() {
		if (instance == null) {
			instance = new CartDao();
		}
		return instance;
	}

	/**
	 * 插入一条购买商品信息
	 * @param good
	 */
	public void insertGood(Cart good) {
		try {
			userDao = DataBaseHelper.getHelper(context).getCartDao();
			userDao.createIfNotExists(good);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 查询出所有购物车呢的商品
	 */
	public List<Cart> queryAllGood() {
		List<Cart> list=null;
		try {
			userDao = DataBaseHelper.getHelper(context).getCartDao();
			list = userDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 更新一个
	 * @param good
	 */
	public void updateoneGood(Cart good) {
		try {
			userDao = DataBaseHelper.getHelper(context).getCartDao();
			 userDao.update(good);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 通过goodid 获取购物车内这个商品的个数
	 * @param goodid
	 */
	public int queryNumbygoodid(int goodid) {
		try {
			userDao = DataBaseHelper.getHelper(context).getCartDao();
			List<Cart> list = userDao.queryForEq("goodid",goodid);
			if(list!=null){
				if(list.size()>0){
					return list.get(0).getNum();
				}
			}else{
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 查询数据库中所有商品的数量
	 */
	public int queryNumAll() {
		try {
			userDao = DataBaseHelper.getHelper(context).getCartDao();
			List<Cart> list = userDao.queryForAll();
			if(list!=null){
				if(list.size()>0){
					int num=0;
					for (int i = 0; i < list.size(); i++) {
						num+=queryNumbygoodid(list.get(i).getGoodid());
					}
					return num;
				}
			}else{
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 通过goodid 获取购物车条目对象
	 * @param goodid
	 */
	public Cart queryCartbygoodid(int goodid) {
		try {
			userDao = DataBaseHelper.getHelper(context).getCartDao();
			List<Cart> list = userDao.queryForEq("goodid",goodid);
			if(list!=null){
				if(list.size()>0){
					return list.get(0);
				}
			}else{
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
	/**
	 * 清空购物车数据表内的数据
	 */
	public void deleteAllCart() {
		try {
			userDao = DataBaseHelper.getHelper(context).getCartDao();
			userDao.delete(userDao.queryForAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 删除购物车中数据
	 */
	public void deleteCarts(List<Cart> list) {
		try {
			userDao = DataBaseHelper.getHelper(context).getCartDao();
			userDao.delete(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
