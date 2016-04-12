package com.example.main.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.main.login.dialog.RegisterDialog;
import com.example.main.login.database.LoginDataBase;
import com.example.main.login.server.JSONHttpUtil;
import com.example.main.login.util.BitmapCircular;
import com.example.main.login.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends Activity implements OnClickListener {
	private LinearLayout layout;
	private ImageView head;
	private EditText account;
	private Button AButton;
	private RelativeLayout list_layout;
	private View list_view;
	private EditText Cipher;
	private Button login;
	private TextView register;
	private SharedPreferences sp;
	private SQLiteDatabase db;
	private Cursor InfoCursor;
	private HorizontalListView listView;
	private MyAdapter myAdapter;
	private boolean isButton = false;
	private List<String> listAccount;
	private List<String> listCipher;
	private TranslateAnimation mShowlayout;
	private TranslateAnimation mShowCipher;
	private Timer timer;
	private ImageView list_image;
	private TextView list_show;
	private Message msg;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_avtivity);
		init();
	}

	// ��ʼ���ؼ�
	private void init() {
		layout = (LinearLayout) findViewById(R.id.login_layout);
		head = (ImageView) findViewById(R.id.login_Head);
		account = (EditText) findViewById(R.id.account);
		Cipher = (EditText) findViewById(R.id.Cipher);
		AButton = (Button) findViewById(R.id.account_Buttom);
		listView = (HorizontalListView) findViewById(R.id.login_list);
		list_view = (View) findViewById(R.id.list_view);
		list_layout = (RelativeLayout) findViewById(R.id.list_layout);
		login = (Button) findViewById(R.id.login);
		register = (TextView) findViewById(R.id.register);

		InfoSize(); // �������óߴ�
		SP(); // ���ش洢
		BitmapCircular(); // ͷ��Բ�δ���

		db = new LoginDataBase(LoginActivity.this).getReadableDatabase();
		InfoCursor = db.query("InfoTable", null, null, null, null, null, null);

		account.setSelection(account.getText().length()); // ����������

		InfoAccount();
		if (listAccount.size() != 0) {
			AButton.setVisibility(View.VISIBLE);
		}
	}

	// �Բ��ֳߴ�����Ż�
	private void InfoSize() {
		int height;
		// ��ȡ��Ļ�ߴ�
		WindowManager wm = this.getWindowManager();
		int w = wm.getDefaultDisplay().getWidth();
		int h = wm.getDefaultDisplay().getHeight();

		if (h > 1000) {
			height = 16;
		} else {
			height = 11;
		}

		// ���ÿؼ��ߴ�
		LayoutParams lpe1; // �˺ű༭��
		lpe1 = (LayoutParams) account.getLayoutParams();
		lpe1.width = w;
		lpe1.height = h / height;
		account.setLayoutParams(lpe1);

		LayoutParams lpe2; // ����༭��
		lpe2 = (LayoutParams) Cipher.getLayoutParams();
		lpe2.width = w;
		lpe2.height = h / height;
		Cipher.setLayoutParams(lpe2);

		LayoutParams lpb; // ��¼��ť
		lpb = (LayoutParams) login.getLayoutParams();
		lpb.width = w - 40;
		lpb.height = h / height;
		login.setLayoutParams(lpb);

		LayoutParams list_pb; // �б�ߴ�
		list_pb = (LayoutParams) list_layout
				.getLayoutParams();
		list_pb.width = w;
		list_pb.height = (int) ((h / height) * 2.5);
		list_layout.setLayoutParams(list_pb);

	}

	// ��ȡ�ѱ�����˺�����
	public void SP() {

		sp = getSharedPreferences("info", MODE_PRIVATE);
		if (sp.getBoolean("Remember", false)) {
			account.setText(sp.getString("name", ""));
			Cipher.setText(sp.getString("pwd", ""));

			// �ж��Զ���¼
			if (sp.getBoolean("Automatic", false)) {
				startActivity(new Intent(LoginActivity.this, MainActivity.class));

			}

		}
	}

	// �����Ե�¼����˺�
	public void InfoAccount() {
		listAccount = new ArrayList<String>();
		listCipher = new ArrayList<String>();

		for (int i = 0; i < InfoCursor.getCount(); i++) {
			InfoCursor.moveToPosition(i);

			listAccount.add(InfoCursor.getString(0));
			listCipher.add(InfoCursor.getString(1));

		}

	}

	// ͷ����
	public void BitmapCircular() {
		Bitmap Resources = BitmapFactory.decodeResource(getResources(),
				R.drawable.head);
		Bitmap bitmap = new BitmapCircular().setCircular(Resources, 200.0f);
		head.setImageBitmap(bitmap);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		db = new LoginDataBase(LoginActivity.this).getReadableDatabase();
		InfoCursor = db.query("InfoTable", null, null, null, null, null, null);
	}

	@Override
	protected void onStart() {
		super.onStart();
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		AButton.setOnClickListener(this);
		layout.setOnClickListener(this);
		listView.setOnItemClickListener(new ListItemClickListener());
	}

	@Override
	protected void onStop() {
		super.onStop();
		db.close();
		InfoCursor.close();
		finish();
	}

	// �����¼�����
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// ��¼
		case R.id.login:
			new Thread(new Runnable() {

				@Override
				public void run() {
					synchronized (this) {
						msg = new Message();
					}
					String Response = "";
					Response = new JSONHttpUtil().doLogin(account.getText()
							.toString().trim(), Cipher.getText().toString()
							.trim());

					if (Response.equals("OK")) { // ��¼�ɹ�
						Automatic(); // ���˺ű�������ݿ�
						Remember(); // ������¼���˺ű���������

						startActivity(new Intent(
								LoginActivity.this, MainActivity.class));
					} else if (Response.equals("NOPassword")) { // �������
						msg.what = 1;
						msg.obj = "�������";
						LoginHandler.sendMessage(msg);
					} else if (Response.equals("NO")) { // �˺�δע��
						msg.what = 2;
						msg.obj = "�˺�δע��";
						LoginHandler.sendMessage(msg);
					}

				}
			}).start();

			break;

		// ע��
		case R.id.register:
			Dialog RegisterDialog = new RegisterDialog(this,
					RegisterDialogHandler);
			RegisterDialog.show();
			break;

		// �б?ť
		case R.id.account_Buttom:

			if (!isButton) { // ��ť����
				AButton.setBackgroundResource(R.drawable.text_button_);
				isButton = true;

				OpenView(); // չ���б�

				list_layout.setVisibility(View.VISIBLE);
				list_view.setVisibility(View.VISIBLE);

				myAdapter = new MyAdapter();
				listView.setAdapter(myAdapter);

			} else { // ��ť����
				AButton.setBackgroundResource(R.drawable.text_button);
				isButton = false;

				ContView(); // �����б�

				// �ӳ�0.5��
				timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						Message msg = new Message();
						TimerHandler.sendMessage(msg);
					}
				}, 500);

			}

			break;

		// �ر�����̺��б��
		case R.id.login_layout:
			AButton.setBackgroundResource(R.drawable.text_button);

			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

			// �ж��Ƿ���������б�
			if (isButton) {

				ContView(); // �����б�

				// �ӳ�0.5��
				timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						Message msg = new Message();
						TimerHandler.sendMessage(msg);
					}
				}, 510);
				isButton = false;
			}

			break;

		default:
			break;
		}

	}

	// �б�Item�����¼�
	class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			account.setText(listAccount.get(position));
			Cipher.setText(listCipher.get(position));
			login.performClick();

		}

	}

	// �˺ű�������ݿ�
	private void Automatic() {
		boolean is = true;
		// ���˺��Ƿ񱣴��
		for (int j = 0; j < InfoCursor.getCount(); j++) {
			InfoCursor.moveToPosition(j);
			if (account.getText().toString().equals(InfoCursor.getString(0))) {
				is = false;
			}
		}

		// ��������ݿ�
		if (is) {
			ContentValues values = new ContentValues();
			values.put("name", account.getText().toString());
			values.put("pwd", Cipher.getText().toString());
			db.insert("InfoTable", null, values);
		}
	}

	// �Զ���¼�ͼ�ס���빦��
	private void Remember() {
		sp.edit().putBoolean("Automatic", true).commit();
		sp.edit().putBoolean("Remember", true).commit();
		sp.edit().putString("name", account.getText().toString())
				.putString("pwd", Cipher.getText().toString()).commit();
	}

	// List������
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listAccount.size();
			// return 2;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) { // ��һ�δ򿪼��ؿؼ�
				convertView = getLayoutInflater().inflate(R.layout.login_item,
						null);
				list_image = (ImageView) convertView
						.findViewById(R.id.login_item_image);
				list_show = (TextView) convertView
						.findViewById(R.id.login_item_text);

			}

			list_show.setText(listAccount.get(position));

			Bitmap Resources = BitmapFactory.decodeResource(getResources(),
					R.drawable.p3);
			Bitmap bitmap = new BitmapCircular().setCircular(Resources, 220.0f);
			list_image.setImageBitmap(bitmap);

			return convertView;
		}

	}

	// ��¼�������
	@SuppressLint("HandlerLeak")
	Handler LoginHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // �������
				Cipher.setText("");
				break;

			case 2: // �˺�Ϊע��
				break;

			default:
				break;
			}

			Toast.makeText(LoginActivity.this, (String) msg.obj,
					Toast.LENGTH_SHORT).show();

		}
	};

	// ע��Ի���ķ������
	@SuppressLint("HandlerLeak")
	Handler RegisterDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String[] strings = ((String) msg.obj).split("-");

				for (int i = 0; i < strings.length; i++) {
					Log.e("strings" + i, strings[i]);
				}

				account.setText(strings[0]);
				Cipher.setText(strings[1]);
				Toast.makeText(LoginActivity.this, "ע��ɹ�", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}

		}
	};

	// չ���б?��Ч��
	public void OpenView() {
		// չ���б�
		mShowlayout = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowlayout.setDuration(500);
		list_layout.startAnimation(mShowlayout);

		// �����ƶ������
		mShowCipher = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-2.5f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowCipher.setDuration(500);
		Cipher.startAnimation(mShowCipher);

		login.startAnimation(mShowCipher); // �����ƶ���¼��ť
	}

	// �����б?��Ч��
	public void ContView() {
		// �����б�
		mShowlayout = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		mShowlayout.setDuration(500);
		list_layout.startAnimation(mShowlayout);

		// �����ƶ������
		mShowCipher = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -2.5f);
		mShowCipher.setDuration(500);
		Cipher.startAnimation(mShowCipher);

		login.startAnimation(mShowCipher); // �����ƶ���¼��ť

	}

	// �����б?��
	Handler TimerHandler = new Handler() {
		public void handleMessage(Message msg) {
			list_view.setVisibility(View.GONE);
			list_layout.setVisibility(View.GONE);
		}
	};

}
