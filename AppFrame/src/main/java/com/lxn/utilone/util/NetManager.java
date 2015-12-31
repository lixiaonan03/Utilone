package com.lxn.utilone.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lxn.utilone.UtilApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Description:网络管理的类
 * @time:2014年9月21日 下午2:54:12
 */
public class NetManager {
	private Context context;
	private static NetManager netManager;

	public NetManager(Context context) {
		super();
		this.context = context;
	}
	/**
	 * 单例模式获取网络工具类
	 * @return
	 */
	public static NetManager getInstance() {
		if (null == netManager) {
			netManager = new NetManager(UtilApplication.getInstance());
		}
		return netManager;
	}
	/**
	 * 判断是否有网络连接
	 */
	public boolean hasInternetConnected() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}
	 /**
     * 判断是否有网络没有网络弹出wifi提示
     */
	public boolean validateInternet(Context contextshow) {
		ConnectivityManager manager = (ConnectivityManager) contextshow
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			openWifiSet(contextshow);
			return false;
		} else {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
				openWifiSet(contextshow);
			}
		}
		return false;
	}
    /**
     * 弹出wif设置的dialog
     */
	public void openWifiSet(final Context contextshow) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(contextshow);
		dialogBuilder
				.setTitle("提示")
				.setMessage("无可用网络连接，请检查网络设置！")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						 //Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
						// wifi设置
						Intent intent = new Intent(
								android.provider.Settings.ACTION_WIFI_SETTINGS);
						contextshow.startActivity(intent);
					}
				})
				.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		dialogBuilder.show();
	}

	/**
	 * 获取ip 地址 没连网 返回null
	 * @return
	 */
	public String getIP() {
		String ip = null;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				if (network.getType() == ConnectivityManager.TYPE_WIFI) {
					// 获取 IP地址
					WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					int ipAddress = wifiInfo.getIpAddress();
					ip = intToIp(ipAddress);
				}
				if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
					try {
						Enumeration allNetInterfaces = NetworkInterface
								.getNetworkInterfaces();
						InetAddress ip1 = null;
						while (allNetInterfaces.hasMoreElements()) {
							NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
									.nextElement();
							Enumeration addresses = netInterface
									.getInetAddresses();
							while (addresses.hasMoreElements()) {
								ip1 = (InetAddress) addresses.nextElement();
								if (ip1 != null && ip1 instanceof Inet4Address) {
									if (!ip1.getHostAddress().equals(
											"127.0.0.1")) {
										ip = ip1.getHostAddress();
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ip;
	}
	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
	 /**
     * 判断为哪个运营商 
     * 46000 中国移动 （gsm 900 / gsm 1800）
       46001 中国联通 （gsm 900 / umts 2100）
       46002 中国移动 （td-scdma 2010）
       46003 中国电信（cdma 800）（原联通CDMA）
       46004 空（似乎是专门用来做测试的）
       46005 中国电信 （cdma 2000）
       46006 中国联通 （umts 2100）
       46007 中国移动 （td-scdma 2010）
       @return   China_Mobile 移动  China_Unicom 联通 China_Telecom 电信
     */
    public  String getTelecomOperators(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        String imsi = "";
        if (networkInfo != null && networkInfo.isAvailable()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            imsi = telephonyManager.getSubscriberId();
        }
        if(!TextUtils.isEmpty(imsi)){
            if(imsi.startsWith("46000") || imsi.startsWith("46002")||imsi.startsWith("46007")){
                return "China_Mobile";
            }
            else if(imsi.startsWith("46001")||imsi.startsWith("46006")){
                return "China_Unicom";
            }
            else if(imsi.startsWith("46003")||imsi.startsWith("46005")){
                return "China_Telecom";
            }
        }
        return "";
    }
	/**
     * 判断sim卡网络为几代网络
     * @return  0没有 1未知 2 2G 3 3G 4 4G
     */
    public  int getNetGeneration(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return 2;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return 2;
                case TelephonyManager.NETWORK_TYPE_LTE:

                    return 4;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return 1;
                default:
                    return 1;
            }
        }
        else {
            return 0;
        }
    }
    /**
	 * 得到当前的手机网络类型
	 * @return
	 */
	public  String getCurrentNetType() {
		String type = "";
		ConnectivityManager cm = (ConnectivityManager)context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			type = "null";
		} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			type = "wifi";
		} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

			int subType = info.getSubtype();
			if (subType == TelephonyManager.NETWORK_TYPE_CDMA
					|| subType == TelephonyManager.NETWORK_TYPE_GPRS
					|| subType == TelephonyManager.NETWORK_TYPE_EDGE
					//高速网络 先算在3G
					||subType==TelephonyManager.NETWORK_TYPE_HSPAP) {
				type = "2g";
			} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
					|| subType == TelephonyManager.NETWORK_TYPE_HSDPA
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_0
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B
					) {
				type = "3g";
			} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {
				// LTE是3g到4g的过渡，是3.9G的全球标准
				type = "4g";
			}
		}
		return type;
	}
}
