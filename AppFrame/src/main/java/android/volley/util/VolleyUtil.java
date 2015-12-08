package android.volley.util;

import android.content.Context;
import android.volley.constant.GlobConstant;
import android.volley.listener.HttpBackBeanListener;
import android.volley.listener.HttpBackListListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lxn.utilone.UtilApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名：VolleyUtil
 * 
 * 描述：volley请求封装工具类
 * 
 */
public class VolleyUtil {
	private static RequestQueue requestQueue;

	private final static int TIME_OUT = 15000;

	/**
	 * 解析返回json的节点字段
	 */
	private final static String RESCODE = "retCode";
	private final static String FAILSTRING = "errorDesc";
	private final static String RESPONSEBODY = "responseBody";

	/**
	 * Construct 构造方法
	 */
	private VolleyUtil() {
	}

	/**
	 * 使用单例模式取得RequestQueue 对象
	 * 
	 * @return
	 */
	public static RequestQueue getInstance() {
		if (requestQueue == null) {
			synchronized (VolleyUtil.class) {
				if (requestQueue == null) {
					requestQueue = Volley
							.newRequestQueue(UtilApplication.application);
					requestQueue.start();
				}
			}
		}
		return requestQueue;
	}

	public static <T> void addRequest(RequestQueue requestQueue,
			Request<T> request, Object tag) {
		if (tag != null) {
			request.setTag(tag);
		}
		request.setShouldCache(false);
		request.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		requestQueue.add(request);
	}

	public static void cancelAllByTag(Object tag) {
		if (null != requestQueue) {
			if (tag != null) {
				requestQueue.cancelAll(tag);
			}
		}
	}

	public static void cancelAll(Context context) {
		if (null != requestQueue) {
			requestQueue.cancelAll(context);
		}
	}

	/**
	 * 通过post方式 发送StringRequest 请求 请求结果返回的是装换成的实体类对象
	 * 
	 * @param url
	 *            请求url
	 * @param tag
	 *            请求tag
	 * @param params
	 *            请求参数
	 * @param clazz
	 *            请求结果需要转换成的实体类
	 * @param listener
	 *            请求处理接口实现
	 * @param ishascookie
	 *            是否登录
	 * @param cookieValue
	 *            如果登录 需要设置的cookie值
	 */
	public static <T> void sendStringRequestByPost(String url, Object tag,
			final Map<String, String> params, final Class<T> clazz,
			final HttpBackBeanListener<T> listener, final boolean ishascookie,
			final String cookieValue) {
		StringRequest stringRequest = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String s) {
						JSONObject object = JSON.parseObject(s);
						String code = (String) object.get(RESCODE);
						if ("200".equals(code)) {
							JSONObject response = (JSONObject) object
									.get(RESPONSEBODY);
							T t = JSON.parseObject(response.toJSONString(),
									clazz);
							listener.onSuccess(t);
						} else {
							String failstring = (String) object.get(FAILSTRING);
							listener.onFail(failstring);
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (volleyError != null) {
							listener.onError(volleyError);
						}
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				if (ishascookie) {
					headers.put(GlobConstant.COOKIE, cookieValue);
				}
				return headers;
			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params == null ? super.getParams() : params;
			}

		};
		addRequest(getInstance(), stringRequest, tag);
	}

	/**
	 * 通过post方式 发送StringRequest 请求  请求结果返回的是装换成的实体类对象
	 * @param url
	 *            请求url
	 * @param tag
	 *            请求tag
	 * @param clazz
	 *            请求结果需要转换成的实体类
	 * @param listener
	 *            请求处理接口实现
	 */
	public static <T> void sendStringRequestByPost(String url, Object tag,
			final Class<T> clazz,
			final HttpBackBeanListener<T> listener) {
		StringRequest stringRequest = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String s) {
						JSONObject object = JSON.parseObject(s);
						T t = JSON.parseObject(object.toJSONString(), clazz);
						listener.onSuccess(t);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (volleyError != null) {
							listener.onError(volleyError);
						}
					}
				}) {
		};
		addRequest(getInstance(), stringRequest, tag);
	}

	/**
	 * 通过get方式 发送StringRequest 请求 结果返回的是list对象
	 * 
	 * @param url
	 *            请求url
	 * @param tag
	 *            请求tag
	 * @param clazz
	 *            请求结果需要转换成的实体类
	 * @param listener
	 *            请求处理接口实现
	 */
	public static <T> void sendStringRequestByGetToList(final String url,
			Object tag,final Class<T> clazz,
			final HttpBackListListener<T> listener) {
		StringRequest stringRequest = new StringRequest(Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						JSONArray object = JSON.parseArray(s);
							List<T> t = JSON.parseArray(object.toJSONString(), clazz);
							listener.onSuccess(t);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (volleyError != null) {
							listener.onError(volleyError);
						}
					}
				}) {
			
		};
		addRequest(getInstance(), stringRequest, tag);
	}
	/**
	 * 通过post请求返回list
	 * @param url
	 * @param tag
	 * @param map
	 * @param clazz
	 * @param listener
	 */
	public static <T> void sendStringRequestByPostToList(final String url,
			Object tag,final Map<String,String> map,final Class<T> clazz,
			final HttpBackListListener<T> listener) {
		StringRequest stringRequest = new StringRequest(Method.POST,url,
			    new Response.Listener<String>() {
			        @Override
			        public void onResponse(String response) {
			        	JSONArray object = JSON.parseArray(response);
						List<T> t = JSON.parseArray(object.toJSONString(), clazz);
						listener.onSuccess(t);
			        }
			    }, new Response.ErrorListener() {
			        @Override
			        public void onErrorResponse(VolleyError error) {
			        	if (error != null) {
							listener.onError(error);
						}
			        }
			    }) {
			    @Override
			    protected Map<String, String> getParams() {
			          return map;
			    }
			};        
		addRequest(getInstance(), stringRequest, tag);
	}
	
}
