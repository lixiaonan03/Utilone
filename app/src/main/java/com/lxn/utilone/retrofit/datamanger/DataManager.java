package com.lxn.utilone.retrofit.datamanger;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.lxn.utilone.retrofit.RetrofitHelper;
import com.lxn.utilone.retrofit.ToolResultList;
import com.lxn.utilone.retrofit.bean.BindCardRecommendBankBean;
import com.lxn.utilone.retrofit.service.DataService;
import com.lxn.utilone.retrofit.service.Status;

import io.reactivex.Observable;
import retrofit2.Call;


/**
 * 作者：李晓楠
 * 时间：2017/3/13 11:47
 */
public class DataManager {
    private DataService mRetrofitService;
    public DataManager(){
        this.mRetrofitService = RetrofitHelper.getInstance().getRetrofit().create(DataService.class);
    }
    public Observable<ToolResultList<BindCardRecommendBankBean>> getdata(){
        return mRetrofitService.getdata();
    }
}
