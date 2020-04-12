package com.lxn.utilone.util;

import java.sql.Timestamp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonUtil {
    private static Gson gson;
    public static Gson getInstance() {
    	if(null==gson){
    		gson= new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    	}
		return gson;
	}
}
