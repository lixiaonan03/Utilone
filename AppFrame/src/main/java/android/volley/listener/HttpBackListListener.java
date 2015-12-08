package android.volley.listener;

import java.util.List;

import com.android.volley.VolleyError;

/**
 * 类名:		HttpSuccessListener
 * 描述:		通信成功后的接口回调 接口返回的是list数据的
 * @param <T>
 */
public interface HttpBackListListener<T> {

  public void onSuccess(List<T> t);
  /**
   * 业务失败的回调方法
   * @param failstring
   */
  public void onFail(String failstring);
  /**
   * volley框架 请求失败调用的方法
   * @param error
   */
  public void onError(VolleyError error);
}
