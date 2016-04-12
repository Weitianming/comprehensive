package com.example.main.login.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.database.ChatDataBase;
import com.example.main.ChatActivity;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

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
				ChatRecord(sender, content); // ��������Ϣ�����ڱ�����ݿ�

				String activityName = getRunningActivityName();
				if (activityName.equals("com.example.main.MainActivity")) {
					
					// ��ǰ��������������
					
				} else if (activityName.equals("com.example.main.ChatActivity")) {
					if (ChatActivity.name.equals(sender)) { // ���ǵ�ǰ���������
						swapMessage = new SwapMessage();
						swapMessage.setSender(sender);
						swapMessage.setContent(content);
						ChatActivity.swapMessages.add(swapMessage);
						new ChatAdapter().setData(ChatActivity.swapMessages);
						Message msg = new Message();
						msg.what = 4;
						ChatActivity.handler.sendMessage(msg);
					} else { // �ǵ�ǰ���������
						
						// ���񶯴���
						
					}
					
				} else { // �ǵ�ǰ������
					
					// ���񶯡���Ƶ����
					
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	// �����յ���������Ϣ��������ݿ���
	private void ChatRecord(String sender, String content) {
		db = new ChatDataBase(context, sender).getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", sender);
		values.put("content", content);
		db.insert("r"+sender, null, values);
	}

	// ��ȡ��ǰ��Activity������
	private String getRunningActivityName() {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
				.getClassName();
		return runningActivity;
	}
	
}
