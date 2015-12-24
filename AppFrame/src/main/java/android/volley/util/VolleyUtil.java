package android.volley.util;

import android.content.Context;
import android.util.Base64;
import android.volley.constant.GlobConstant;
import android.volley.listener.HttpBackBaseListener;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lxn.utilone.UtilApplication;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
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

	private final static String appid="AD.sunyard@163.com";
	private final static String apppwd="sunyardAD";
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
	public static <T> void sendStringRequestByGetToList(final String url,
														Object tag, final Map<String, String> params, final Class<T> clazz,
														final HttpBackListListener<T> listener, final boolean ishascookie,
														final String cookieValue) {
		StringRequest stringRequest = new StringRequest(Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						JSONObject object = JSON.parseObject(s);
						String code = (String) object.get(RESCODE);
						if ("200".equals(code)) {
							JSONArray response = (JSONArray) object
									.get(RESPONSEBODY);
							List<T> t = JSON.parseArray(
									response.toJSONString(), clazz);
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
				Map<String, String> headers =new HashMap<String, String>();
				headers.put("Authorization", encrypt(appid, apppwd));
				if (ishascookie) {
					headers.put(GlobConstant.COOKIE, cookieValue);
				}
				return headers;
			}

			@Override
			public String getUrl() {
				String sParams = BaseUtil.mapToStringParams(params);
				if (sParams.equals("")) {
					return super.getUrl();
				} else {
					return url + "?" + sParams;
				}
			}
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
	/**
	 * get 方式请求 返回的是 String 内容 （本地服务器接口使用的 有返回值返回返回值 没有返回ok）
	 *
	 * @param url
	 * @param tag
	 * @param params
	 * @param listener
	 * @param ishascookie
	 * @param cookieValue
	 */
	public static void sendStringRequestByGetToString(final String url,
													  Object tag, final Map<String, String> params,
													  final HttpBackBaseListener listener,final boolean ishascookie,
													  final String cookieValue) {
		StringRequest stringRequest = new StringRequest(Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						JSONObject object = JSON.parseObject(s);
						String code = (String) object.get(RESCODE);
						if ("200".equals(code)) {
							if (object.containsKey(RESPONSEBODY)) {
								String response = (String) object
										.get(RESPONSEBODY);
								listener.onSuccess(response);
							} else {
								listener.onSuccess("ok");
							}
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
				headers.put("Authorization", encrypt(appid, apppwd));
				if (ishascookie) {
					headers.put(GlobConstant.COOKIE, cookieValue);
				}
				return headers;
			}

			@Override
			public String getUrl() {
				String sParams = BaseUtil.mapToStringParams(params);
				if (sParams.equals("")) {
					return super.getUrl();
				} else {
					return url + "?" + sParams;
				}
			}
		};
		addRequest(getInstance(), stringRequest, tag);
	}
	/**
	 * 通过get方式 发送StringRequest 请求 返回结果直接是对象
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
	 *            是否有cookie
	 * @param cookieValue
	 *            需要设置的cookie值
	 */
	public static <T> void sendStringRequestByGetToBean(final String url,
														Object tag, final Map<String, String> params, final Class<T> clazz,
														final HttpBackBeanListener<T> listener, final boolean ishascookie,
														final String cookieValue) {
		StringRequest stringRequest = new StringRequest(Method.GET, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String s) {
						JSONObject object = JSON.parseObject(s);
						String code = (String) object.get(RESCODE);
						if ("200".equals(code)) {
							JSONObject response = (JSONObject) object
									.get(RESPONSEBODY);
							if (response == null)
								response = new JSONObject();
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
				headers.put("Authorization", encrypt(appid, apppwd));
				if (ishascookie) {
					headers.put(GlobConstant.COOKIE, cookieValue);
				}
				return headers;

			}

			@Override
			public String getUrl() {
				String sParams = BaseUtil.mapToStringParams(params);
				if (sParams.equals("")) {
					return super.getUrl();
				} else {
					return url + "?" + sParams;
				}
			}
		};
		addRequest(getInstance(), stringRequest, tag);
	}
	/**
	 * 向服务器发送一个post json 请求 返回是个list对象 传输的参数是个对象  分页
	 *
	 * @param url
	 *            地址
	 * @param tag
	 *            标签
	 * @param object
	 *            传输的对象
	 * @param clazz
	 *            返回的list中实体类
	 * @param listener
	 * @param ishascookie
	 * @param cookieValue
	 */
	public static <T> void sendObjectByPostToListBypage(final String url, Object tag,
														final Object object, final Class<T> clazz,
														final HttpBackListListener<T> listener, final boolean ishascookie,
														final String cookieValue) {
		JsonObjectRequest jsonrequest = new JsonObjectRequest(Method.POST, url,
				null, new Response.Listener<org.json.JSONObject>() {

			@Override
			public void onResponse(org.json.JSONObject paramT) {
				String code;
				try {
					code = (String) paramT.get(RESCODE);
					if ("200".equals(code)) {
						org.json.JSONObject response = (org.json.JSONObject) paramT
								.get(RESPONSEBODY);
						org.json.JSONArray result = (org.json.JSONArray) response
								.get("result");
						List<T> t = JSON.parseArray(
								result.toString(), clazz);
						listener.onSuccess(t);
						/*org.json.JSONArray response = (org.json.JSONArray) paramT
								.get(RESPONSEBODY);
						List<T> t = JSON.parseArray(
								response.toString(), clazz);
						listener.onSuccess(t);*/
					} else {
						String failstring = (String) paramT
								.get(FAILSTRING);
						listener.onFail(failstring);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError paramVolleyError) {
				if (paramVolleyError != null) {
					listener.onError(paramVolleyError);
				}
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				headers.put("Authorization", encrypt(appid, apppwd));
				if (ishascookie) {
					headers.put(GlobConstant.COOKIE, cookieValue);
				}
				return headers;
			}

			@Override
			public byte[] getBody() {
				String objectstr = JSONObject.toJSONString(object);
				try {
					return objectstr.getBytes("utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return super.getBody();
			}
		};
		addRequest(getInstance(), jsonrequest, tag);
	}
	/**
	 * 接口加密算法
	 * @param appId
	 * @param appPwd
	 * @return  string 加密完的字符串
	 */
	public static String encrypt(String appId, String appPwd) {
		String v = appId + ":" + appPwd;
		String base= Base64.encodeToString(v.getBytes(), Base64.DEFAULT);
		return base;
	}
}
