package com.example.main.login.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONHttpUtil {
	private String Response = "";
	private JSONObject jsonParam = new JSONObject();
	public static String baseIp = "114.215.95.214";
	public static String urlStr = "http://114.215.95.214/MyServersText/HelloWorld";

	/**
	 * ����
	 * @param jsonObject ��Ҫ���͵�Json���
	 * @return ������Json���
	 */
	private JSONObject send(JSONObject jsonObject) {
		JSONObject object = new JSONObject();
		// Post����
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
				Log.e("���ͳɹ�", string);
			} else {
				Log.e("httpResponse", "����ʧ��");
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
	 * ��¼����
	 * @param userName �û���
	 * @param userPwd ����
	 * @return ����String
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
	 * ע������
	 * @param userName �û���
	 * @param userPwd ����
	 * @return ����String
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
	 * ��ȡ������Ϣ
	 * @param sender ��Ҫ��ȡ���ѵ��˺�
	 * @return ����String
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
	 * ������Ϣ
	 * @param sender ���ͷ�
	 * @param receiver ���շ�
	 * @param notice ��Ϣ����
	 * @return ����String
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
