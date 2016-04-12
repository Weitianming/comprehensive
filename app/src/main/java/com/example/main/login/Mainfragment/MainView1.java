package com.example.main.login.Mainfragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.main.login.ChatActivity;
import com.example.main.login.MainActivity;
import com.example.main.login.R;
import com.example.main.login.ibtn.IBtnCallListener;
import com.example.main.login.server.JSONHttpUtil;
import com.example.main.login.util.BitmapCircular;

public class MainView1 extends Fragment {
	boolean isTitle;
	private IBtnCallListener mBtnCallListener;
	private View rootView;
	private ImageView view1_title_picure;
	private Button view1_title_news, view1_title_call;
	private ImageButton view1_title_button;
	
	private String[] Names;
	private ListView listView;
	private MyAdapter myAdapter;
	private Message msg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.main_view1, container, false);

		initTitleView(); // �Ϸ��˵�
		initDragLayout(); // �����򿪻�������
		
		initView(); // ��ʼ���ؼ�
		getAllUsersName(); // ��ȡ���˺ŵ����к���
		

		return rootView;
	}

	// �Ϸ��˵�
	private void initTitleView() {
		view1_title_news = (Button) rootView
				.findViewById(R.id.view1_title_news);
		view1_title_call = (Button) rootView
				.findViewById(R.id.view1_title_call);

		isTitle = false;
		view1_title_news.setBackgroundResource(R.drawable.title_news_1);
		view1_title_call.setBackgroundResource(R.drawable.title_call_0);
		view1_title_news.setTextColor(Color.parseColor("#00abec"));
		view1_title_call.setTextColor(Color.parseColor("#cceffc"));

		view1_title_news.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isTitle) {
					view1_title_news
							.setBackgroundResource(R.drawable.title_news_1);
					view1_title_call
							.setBackgroundResource(R.drawable.title_call_0);
					view1_title_news.setTextColor(Color.parseColor("#00abec"));
					view1_title_call.setTextColor(Color.parseColor("#cceffc"));
					isTitle = false;
				}
			}
		});

		view1_title_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isTitle) {
					view1_title_call
							.setBackgroundResource(R.drawable.title_call_1);
					view1_title_news
							.setBackgroundResource(R.drawable.title_news_0);
					view1_title_news.setTextColor(Color.parseColor("#cceffc"));
					view1_title_call.setTextColor(Color.parseColor("#00abec"));
					isTitle = true;
				}
			}
		});
	}

	// �����򿪻�������
	private void initDragLayout() {
		view1_title_picure = (ImageView) rootView
				.findViewById(R.id.view1_title_picure);
		view1_title_button = (ImageButton) rootView
				.findViewById(R.id.view1_title_button);

		// ������ͷ��
		Bitmap Resources = BitmapFactory.decodeResource(getResources(),
				R.drawable.p6_1);
		Bitmap bitmap = new BitmapCircular().setCircular(Resources, 200.0f);
		view1_title_picure.setImageBitmap(bitmap);

		// ���
		view1_title_picure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mBtnCallListener.trans();
			}
		});
		// �Ҳ�
		view1_title_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
	}

	// ʵ��ӿ�
	@Override
	public void onAttach(Activity activity) {

		try {
			mBtnCallListener = (IBtnCallListener) activity;
		} catch (Exception e) {
		}

		super.onAttach(activity);
	}
	
	// ��ʼ���ؼ�
	public void initView() {
		listView = (ListView) rootView.findViewById(R.id.view1_list);
		listView.setOnItemClickListener(new ListItemClickListener());
	}
	
	// ���������
	class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.putExtra("name", Names[position]);
			intent.setClass(getActivity(), ChatActivity.class);
			startActivity(intent);
		}
		
	}
	
	
	
	// �����б�
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return Names.length;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater(null);
			convertView = inflater.inflate(R.layout.view1_list, null);
			
			TextView list1_show1 = (TextView) convertView.findViewById(R.id.list1_show1);
			list1_show1.setText(Names[position]);
			
			return convertView;
		}
		
	}
	
	/**  
     * ���¼���ListView�ĸ߶ȣ����ScrollView��ListView����View���й�����Ч����Ƕ��ʹ��ʱ���ͻ������  
     * @param listView  
     */  
    public void setListViewHeight(ListView listView) {    
            
        // ��ȡListView��Ӧ��Adapter    
        
        ListAdapter listAdapter = listView.getAdapter();    
        
        if (listAdapter == null) {    
            return;    
        }    
        int totalHeight = 0;    
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()������������Ŀ    
            View listItem = listAdapter.getView(i, null, listView);    
            listItem.measure(0, 0); // ��������View �Ŀ��    
            totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�    
        }    
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();    
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));    
        listView.setLayoutParams(params);    
    }    
	
	
	// ��ȡ���˺��µ����к���
	private void getAllUsersName() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String Response = "";
				Response = new JSONHttpUtil().getUsersName(MainActivity.name);
				Names = Response.split("/");
				
				if (Names[0].equals("")) {
					Names = new String[0];
				}
				
				msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);

			}
		}).start();
		
	}
	
	
	// ��������֪ͨ
	
	// ���պ��ѷ��͵���Ϣ
	
	
	
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 1:
				myAdapter = new MyAdapter();
				listView.setAdapter(myAdapter);
				setListViewHeight(listView);
				break;
				
			case 2:
				
				break;

			default:
				break;
			}

			
		}
	};

}
