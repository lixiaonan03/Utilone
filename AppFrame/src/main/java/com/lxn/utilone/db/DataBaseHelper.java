package com.lxn.utilone.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 操作数据库的帮助类，使用了OrmLite框架
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "shopcart.db";
	private static final int DATABASE_VERSION = 1;
	private Dao<Cart, Integer> cartDao = null;

	private DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Cart.class);
			cartDao = getCartDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Cart.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获得购物车的dao
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Dao<Cart, Integer> getCartDao() throws SQLException {
		if (cartDao == null) {
			cartDao = getDao(Cart.class);
		}
		return cartDao;
	}
	private static DataBaseHelper instance;

	/**
	 * 单例获取该Helper
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized DataBaseHelper getHelper(Context context) {
		if (instance == null) {
			synchronized (DataBaseHelper.class) {
				if (instance == null){
					instance = new DataBaseHelper(context);
				}
			}
		}
		return instance;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
		cartDao = null;
	}
}
