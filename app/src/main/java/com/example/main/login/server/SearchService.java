package com.example.main.login.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.main.MainActivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SearchService extends Service {
	String IP = "http://yukiy.sinaapp.com/ChatServer";
	int Port = 5123;;
	DataInputStream in;
	DataOutputStream out;
	Socket socket = null;
	JSONObject jsonObject;
	private BufferedReader reader;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		new MessageThread().start();
	}

	class MessageThread extends Thread {
		@Override
		public void run() {
			jsonObject = new JSONObject();

			try {
				socket = new Socket(IP, Port);
//				out = new DataOutputStream(socket.getOutputStream());
//				in = new DataInputStream(socket.getInputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				
//				Log.e("���͵���Ϣasd", MainActivity.name);
//				jsonObject.put("sender", MainActivity.name);
//				String string = jsonObject.toString();
				
//				out.writeUTF(string);
				
			} catch (IOException e) {
				e.printStackTrace();
//			} catch (JSONException e) {
//				e.printStackTrace();
			}
			Log.e("���͵���Ϣ", jsonObject.toString());
			String string = "δ�յ���Ϣ";
			while (true) {

				try {
					Thread.sleep(1000);
					Log.e("����", "1111111111111");
					string = reader.readLine();
					Log.e("����", string);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				Log.e("���ն�", string);
			}

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopSelf();
	}

}
