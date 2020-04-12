package com.lxn.utilone.retrofit;


/**
  *   北京爱钱帮财富科技有限公司
  *   功能描述: 数据返回的数据接口
  *    作 者:  李晓楠
  *    时 间： 2017/3/14 下午5:15 
  */
public class ToolResult<T> {
    private Status  status;//状态
    private T   data;//返回数据

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
