package com.example.main.login;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.main.login.database.ChatDataBase;
import com.example.main.login.server.JSONHttpUtil;
import com.example.main.login.util.ChatAdapter;
import com.example.main.login.util.SwapMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {
	public static String name;
	private TextView chatShow;
	private static ListView chatList;
	private static ChatAdapter chatAdapter;
	private static EditText chatText;
	private Button chatSend;
	private Message msg;
	public static List<SwapMessage> swapMessages;
	private static SwapMessage swapMessage;
	private SQLiteDatabase db;
	private Cursor ChatCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);
		name = getIntent().getStringExtra("name");
		swapMessages = new ArrayList<SwapMessage>();

		// 修改软键盘设置
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
	}

	// 初始化控件
	private void initView() {
		chatShow = (TextView) findViewById(R.id.chat_show);
		chatList = (ListView) findViewById(R.id.chat_list);
		chatText = (EditText) findViewById(R.id.chat_text);
		chatSend = (Button) findViewById(R.id.chat_send);

		chatShow.setText(name);
		chatSend.setOnClickListener(new SendClickListener());
		chatAdapter = new ChatAdapter(this);
		new ChatRecord().start();
	}

	// 发送按钮事件监听
	private class SendClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					synchronized(this) {
						msg = new Message();
						swapMessage = new SwapMessage();
					}
					String string = new JSONHttpUtil().SendNotice(
							MainActivity.name, name, chatText.getText()
									.toString());

					if (string.equals("Ok")) { // 发送成功
						ChatDataBasePreservation(chatText
								.getText().toString()); // 保存发送成功的聊天内容
						swapMessage.setSender(MainActivity.name);
						swapMessage.setContent(chatText.getText().toString());
						swapMessages.add(swapMessage);
						chatAdapter.setData(swapMessages);
						msg.what = 2;
						handler.sendMessage(msg);
					} else { // 发送失败
						msg.what = 3;
						handler.sendMessage(msg);
					}

				}
			}).start();
		}
	}

	// 读取聊天记录的线程
	private class ChatRecord extends Thread {
		
		@Override
		public void run() {
			synchronized (this) {
				msg = new Message();
			}
			msg.what = 1;
			ChatDataBaseHandle(); // 读取存在本地的聊天记录
			chatAdapter.setData(swapMessages);
			
			for (int i = 0; i < swapMessages.size(); i++) {
				
				Log.d("发送者", swapMessages.get(i).getSender());
				Log.d("内容", swapMessages.get(i).getContent());
				
			}
			
			
			handler.sendMessage(msg);
		}
	}
	
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // 本地聊天记录显示
				chatList.setAdapter(chatAdapter);
				chatList.setSelection(chatAdapter.getCount());
				break;
			
			case 2: // 发送成功
				chatAdapter.notifyDataSetChanged();
				chatList.setSelection(chatAdapter.getCount());
				chatText.setText("");
				break;
				
			case 3: // 发送失败
				
				break;
				
			case 4: // 接收信息
				chatAdapter.notifyDataSetChanged();
				chatList.setSelection(chatAdapter.getCount());
				break;

			default:
				break;
			}

		}
	};

	// 本地数据库处理
	private void ChatDataBaseHandle() {
		db = new ChatDataBase(ChatActivity.this, name).getReadableDatabase();
		ChatCursor = db.query("r"+name, null, null, null, null, null, null);
		for (int i = 0; i < ChatCursor.getCount(); i++) {
			ChatCursor.moveToPosition(i);
			swapMessage = new SwapMessage();
			swapMessage.setSender(ChatCursor.getString(0));
			swapMessage.setContent(ChatCursor.getString(1));
			swapMessages.add(swapMessage);
		}
	}

	// 将发送成功的聊天内容保存至本地数据库
	private void ChatDataBasePreservation(String content) {
		ContentValues values = new ContentValues();
		values.put("id", MainActivity.name);
		values.put("content", content);
		db.insert("r"+this.name, null, values);
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

}
