package com.lxn.utilone.util.sign.rsa;

import com.alibaba.fastjson.JSONObject;
import com.lxn.utilone.util.DeviceUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
  *  @copyright:北京爱钱帮财富科技有限公司
  *  功能描述: 对要加签的map 数据做处理的工具类
  *   作 者:  李晓楠
  *   时 间： 2016/8/18 14:20 
 */
public class SortRequestData {
	/**
	 * 对需要加签的map 数据按照key值进行培训
	 * @param map
	 * @return  要加签的数据的MD5值
	 */
	public static String sortString(Map<String, String> map){
		JSONObject json = new JSONObject();
		json.putAll(map);
		Set<String> keySet = json.keySet();
		//可以移除某些不需要加签的数据
		//keySet.remove("token");
		String[] keys = new String[keySet.size()];
		keySet.toArray(keys);
		Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(String key : keys){
			sb.append(key +"="+ json.getString(key));
			if(!key.equals(keys[keys.length-1])){
				sb.append("&");
			}
		}
		return MD5Util.getMd5(sb.toString());
	}
	
	
	public static Map<String, String> getmap(){
		Map<String, String> map = new HashMap<>();
		map.put("device_id", "");
		map.put("system_type", "android");
		map.put("system_version", DeviceUtil.OS_VERSION);
		map.put("app_version", DeviceUtil.APP_VERSIONNAME);
		return map;
	}
}
