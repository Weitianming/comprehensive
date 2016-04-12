package com.example.main.login.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.main.MainActivity;
import com.example.util.ChatHandle;

import android.content.Context;

public class ChatService extends Thread {
	private String IP = "114.215.95.214";
	private int Port = 5123;
	private Socket socket = null;
	private PrintWriter writer;
	private BufferedReader reader;
	private JSONObject jsonObject;
	private String content = null;
	private Context context;
	
	
	public ChatService(Context context) {
		this.context = context;
	}
	
	@Override
	public void run() {
		jsonObject = new JSONObject();

		try {
			socket = new Socket(IP, Port); // ���ӷ�����
			
			// ���͸��˺ŵ�ID��Ϣ
			jsonObject.put("sender", MainActivity.name);
			String string = jsonObject.toString();
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			writer = new PrintWriter(socket.getOutputStream());
			writer.println(string);
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		while (true) {
			try {
				Thread.sleep(1000);
				if ((content  = reader.readLine()) != null) {
					new ChatHandle(content, context).start();
					content = null;
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
