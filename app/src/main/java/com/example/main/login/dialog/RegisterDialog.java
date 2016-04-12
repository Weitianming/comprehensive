package com.example.main.login.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.main.login.R;
import com.example.main.login.server.JSONHttpUtil;

public class RegisterDialog extends Dialog {
	EditText RegisterNumber;
	EditText RegisterPassword;
	EditText ConfirmPassword;
	Button Determine;
	Button Cancel;
	Context context;
	Handler handler;
	String Response = "";
	Message msg;

	public RegisterDialog(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerdialog);
		setTitle(R.string.RegistrationNumber);

		RegisterNumber = (EditText) findViewById(R.id.RegistrationNumber);
		RegisterPassword = (EditText) findViewById(R.id.TheLoginPassword);
		ConfirmPassword = (EditText) findViewById(R.id.ConfirmPassword);
		Determine = (Button) findViewById(R.id.Determine);
		Cancel = (Button) findViewById(R.id.Cancel);
		Determine.setOnClickListener(new Determine());
		Cancel.setOnClickListener(new Cancel());

	}

	class Determine implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					synchronized (this) {
						msg = new Message();
					}
					if (!RegisterNumber.getText().toString().trim().equals("")
							&& !RegisterPassword.getText().toString().trim()
									.equals("")) {
						if (RegisterPassword
								.getText()
								.toString()
								.trim()
								.equals(ConfirmPassword.getText().toString()
										.trim())) {

							Response = new JSONHttpUtil().doRegister(
									RegisterNumber.getText().toString().trim(),
									RegisterPassword.getText().toString()
											.trim());

							if (Response.equals("OK")) {
								msg.what = 1;
								msg.obj = RegisterNumber.getText().toString()
										.trim()
										+ "-"
										+ RegisterPassword.getText().toString()
												.trim();
								handler.sendMessage(msg);
								dismiss();
							} else if (Response.equals("NO")) {
								msg.what = 1;
								msg.obj = "账号已存在";
								Registerhandler.sendMessage(msg);
							}

						} else {
							msg.what = 2;
							msg.obj = "两次输入的密码不同";
							Registerhandler.sendMessage(msg);
						}

					} else {
						msg.what = 3;
						msg.obj = "账号或密码不能为空";
						Registerhandler.sendMessage(msg);
					}
				}
			}).start();

		}
	}

	class Cancel implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	}
	
	Handler Registerhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // 账号已存在
				RegisterNumber.setText("");
				RegisterPassword.setText("");
				ConfirmPassword.setText("");
				break;
				
			case 2: // 两次输入的密码不同
				ConfirmPassword.setText("");
				break;
				
			default:
				break;
				
			}
			Toast.makeText(context, (String) msg.obj,
					Toast.LENGTH_SHORT).show();
		}
	};

}
