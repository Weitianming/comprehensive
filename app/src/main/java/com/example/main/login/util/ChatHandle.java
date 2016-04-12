package com.example.main.login.util;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import com.example.main.login.ChatActivity;
import com.example.main.login.database.ChatDataBase;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatHandle extends Thread {
	private String strContent = null;
	private Context context;
	private JSONObject jsonObject = null;
	private JSONObject object = null;
	private String sender = "";
	private String content = "";
	private SwapMessage swapMessage;
	
	private SQLiteDatabase db;

	public ChatHandle(String strContent, Context context) {
		this.strContent = strContent;
		this.context = context;
	}

	@Override
	public void run() {
		synchronized (this) {
			sender = "";
			content = "";
		}

		try {
			jsonObject = new JSONObject(strContent);

			if (jsonObject.getString("object").equals("notice")) {
				object = new JSONObject(jsonObject.getString("message"));
				sender = object.getString("sender");
				content = object.getString("content");
				ChatRecord(sender, content); // 将聊天信息保存在本地数据库

				String activityName = getRunningActivityName();
				if (activityName.equals("com.example.main.MainActivity")) {

					// 当前窗口在主界面上
					
				} else if (activityName.equals("com.example.main.ChatActivity")) {
					if (ChatActivity.name.equals(sender)) { // 正是当前的聊天好友
						swapMessage = new SwapMessage();
						swapMessage.setSender(sender);
						swapMessage.setContent(content);
						ChatActivity.swapMessages.add(swapMessage);
						new ChatAdapter().setData(ChatActivity.swapMessages);
						Message msg = new Message();
						msg.what = 4;
						ChatActivity.handler.sendMessage(msg);
					} else { // 非当前的聊天好友

						// 做振动处理
						
					}
					
				} else { // 非当前主界面

					// 做振动、音频处理
					
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 将接收到的聊天信息保存至数据库中
	private void ChatRecord(String sender, String content) {
		db = new ChatDataBase(context, sender).getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", sender);
		values.put("content", content);
		db.insert("r"+sender, null, values);
	}

	// 获取当前的Activity的名字
	private String getRunningActivityName() {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
				.getClassName();
		return runningActivity;
	}
	
}
