package com.example.main.login.server;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JSONHttpUtil {
	private String Response = "";
	private JSONObject jsonParam = new JSONObject();
	public static String baseIp = "114.215.95.214";
	public static String urlStr = "http://114.215.95.214/MyServersText/HelloWorld";

	/**
	 * 发送
	 * @param jsonObject 需要发送的Json数据
	 * @return 反馈的Json数据
	 */
	private JSONObject send(JSONObject jsonObject) {
		JSONObject object = new JSONObject();
		// Post请求
		HttpPost post = new HttpPost(urlStr);

		StringEntity se;
		try {
			se = new StringEntity(jsonObject.toString(), "UTF-8");

			post.setEntity(se);
			HttpResponse httpResponse;
			httpResponse = new DefaultHttpClient().execute(post);

			Log.e("post", jsonObject.toString());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String string = EntityUtils.toString(httpResponse.getEntity());
				object = new JSONObject(string);
				Log.e("发送成功", string);
			} else {
				Log.e("httpResponse", "连接失败");
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return object;
	}

	/**
	 * 登录请求
	 * @param userName 用户名
	 * @param userPwd 密码
	 * @return 返回String
	 */
	public String doLogin(String userName, String userPwd) {
		JSONObject object = new JSONObject();

		try {
			object.put("username", userName);
			object.put("password", userPwd);
			jsonParam.put("object", "login");
			jsonParam.put("message", object);
			
			object = send(jsonParam);
			Response = object.getString("LoginResponse");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return Response;
	}

	/**
	 * 注册请求
	 * @param userName 用户名
	 * @param userPwd 密码
	 * @return 返回String
	 */
	public String doRegister(String userName, String userPwd) {
		JSONObject object = new JSONObject();

		try {
			object.put("username", userName);
			object.put("password", userPwd);
			jsonParam.put("object", "register");
			jsonParam.put("message", object);

			object = send(jsonParam);
			Response = object.getString("RegisterResponse");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return Response;
	}

	/**
	 * 获取好友信息
	 * @param sender 需要获取好友的账号
	 * @return 返回String
	 */
	public String getUsersName(String sender) {
		JSONObject object = new JSONObject();
		try {
			object.put("sender", sender);
			jsonParam.put("object", "getAllUsersName");
			jsonParam.put("message", object);

			object = send(jsonParam);
			
			if (object.getString("object").equals("getAllUsersName")) {
				JSONObject jsonObject = new JSONObject(object.getString("message"));
				Response = jsonObject.getString("content");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response;

	}

	/**
	 * 发送消息
	 * @param sender 发送方
	 * @param receiver 接收方
	 * @param notice 消息内容
	 * @return 返回String
	 */
	public String SendNotice(String sender, String receiver,
			String notice) {
		JSONObject object = new JSONObject();
		try {

			object.put("sender", sender);
			object.put("receiver", receiver);
			object.put("content", notice);
			jsonParam.put("object", "notice");
			jsonParam.put("message", object);
			
			object = send(jsonParam);
			Response = object.getString("NoticeResponse");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return Response;
	}

}
