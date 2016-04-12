package com.example.main.login;

import java.util.ArrayList;
import java.util.List;

import com.example.database.ChatDataBase;
import com.example.login.R;
import com.example.server.JSONHttpUtil;
import com.example.util.ChatAdapter;
import com.example.util.SwapMessage;

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

		// �޸����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
	}

	// ��ʼ���ؼ�
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

	// ���Ͱ�ť�¼�����
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

					if (string.equals("Ok")) { // ���ͳɹ�
						ChatDataBasePreservation(chatText
								.getText().toString());// ���淢�ͳɹ�����������
						swapMessage.setSender(MainActivity.name);
						swapMessage.setContent(chatText.getText().toString());
						swapMessages.add(swapMessage);
						chatAdapter.setData(swapMessages);
						msg.what = 2;
						handler.sendMessage(msg);
					} else { // ����ʧ��
						msg.what = 3;
						handler.sendMessage(msg);
					}

				}
			}).start();
		}
	}
	
	// ��ȡ�����¼���߳�
	private class ChatRecord extends Thread {
		
		@Override
		public void run() {
			synchronized (this) {
				msg = new Message();
			}
			msg.what = 1;
			ChatDataBaseHandle(); // ��ȡ���ڱ��ص������¼
			chatAdapter.setData(swapMessages);
			
			for (int i = 0; i < swapMessages.size(); i++) {
				
				Log.d("������", swapMessages.get(i).getSender());
				Log.d("����", swapMessages.get(i).getContent());
				
			}
			
			
			handler.sendMessage(msg);
		}
	}
	
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // ���������¼��ʾ
				chatList.setAdapter(chatAdapter);
				chatList.setSelection(chatAdapter.getCount());
				break;
			
			case 2: // ���ͳɹ�
				chatAdapter.notifyDataSetChanged();
				chatList.setSelection(chatAdapter.getCount());
				chatText.setText("");
				break;
				
			case 3: // ����ʧ��
				
				break;
				
			case 4: // ������Ϣ
				chatAdapter.notifyDataSetChanged();
				chatList.setSelection(chatAdapter.getCount());
				break;

			default:
				break;
			}

		}
	};
	
	// ������ݿ⴦��
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

	// �����ͳɹ����������ݱ�����������ݿ�
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
