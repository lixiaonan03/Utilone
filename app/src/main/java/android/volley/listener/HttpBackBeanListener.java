package android.volley.listener;

import com.android.volley.VolleyError;

/**
 * 类名:		HttpBackBeanListener
 * 描述:		通信成功后的接口回调   接口返回一个实体 
 * @param <T>
 */
public interface HttpBackBeanListener<T> {

  public void onSuccess(T t);
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
