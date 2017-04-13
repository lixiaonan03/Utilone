package com.lxn.utilone.retrofit.service;

import com.alibaba.fastjson.JSONObject;
import com.lxn.utilone.retrofit.ToolResultList;
import com.lxn.utilone.retrofit.bean.BindCardRecommendBankBean;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：李晓楠
 * 时间：2017/3/13 11:41
 */
public interface DataService {
    @GET("cup/capital/is_open_bank_list")
    //Call<JSONObject> getdata();
    Observable<ToolResultList<BindCardRecommendBankBean>> getdata();
}
