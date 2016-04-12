package com.example.main.login.util;

import java.util.ArrayList;
import java.util.List;

import com.example.login.R;
import com.example.main.ChatActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	private Context context;
	private List<SwapMessage> swapMessages;

	public ChatAdapter() {
	}
	
	public ChatAdapter(Context context) {
		swapMessages = new ArrayList<SwapMessage>();
		this.context = context;
	}

	public void setData(List<SwapMessage> swapMessages) {
		this.swapMessages = swapMessages;
	}

	@Override
	public int getCount() {
		return swapMessages.size();
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.chat_item, null);
		}
		RelativeLayout left = (RelativeLayout) convertView
				.findViewById(R.id.left_layout);
		RelativeLayout right = (RelativeLayout) convertView
				.findViewById(R.id.right_layout);
		TextView he_content = (TextView) convertView
				.findViewById(R.id.editText_he_content);
		TextView me_content = (TextView) convertView
				.findViewById(R.id.editText_me_content);

		SwapMessage swapMessage = swapMessages.get(position);
		if (ChatActivity.name.equals(swapMessage.getSender())) {
			left.setVisibility(View.VISIBLE);
			right.setVisibility(View.GONE);
			he_content.setText(swapMessage.getContent());
		} else {
			left.setVisibility(View.GONE);
			right.setVisibility(View.VISIBLE);
			me_content.setText(swapMessage.getContent());
		}

		return convertView;
	}

}
