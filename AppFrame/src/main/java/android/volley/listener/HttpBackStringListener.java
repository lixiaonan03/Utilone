package android.volley.listener;

import com.android.volley.VolleyError;

/**
 * 类名:		HttpBackStringListener
 * 描述:		通信成功后的接口回调  其中成功的方法会直接string 返回回来
 */
public interface HttpBackStringListener {
  /**
   * 成功的方法
   * @param string
   */
  public void onSuccess(String string);
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
