package com.lxn.utilone.retrofit;


import java.util.List;

/**
  *   北京爱钱帮财富科技有限公司
  *   功能描述: 数据返回的数据接口
  *    作 者:  李晓楠
  *    时 间： 2017/3/14 下午5:15 
  */
public class ToolResultList<T> {
    private Status  status;//状态
    private List<T>   data;//返回数据

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
