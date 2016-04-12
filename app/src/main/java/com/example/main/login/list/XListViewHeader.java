/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.example.main.login.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.main.login.R;

public class XListViewHeader extends LinearLayout {

	private static final String HINT_NORMAL = "����ˢ��";
	private static final String HINT_READY = "�ɿ�ˢ�����";
	private static final String HINT_LOADING = "���ڼ���...";

	// ��״̬
	public final static int STATE_NORMAL = 0;
	// ׼��ˢ��״̬��Ҳ���Ǽ�ͷ������ı�֮���״̬
	public final static int STATE_READY = 1;
	// ˢ��״̬����ͷ�����progressBar
	public final static int STATE_REFRESHING = 2;
	// ����������Ҳ���Ǹ��
	private LinearLayout container;
	// ��ͷͼƬ
	private ImageView mArrowImageView;
	// ˢ��״̬��ʾ
	private ProgressBar mProgressBar;
	// ˵���ı�
	private TextView mHintTextView;
	// ��¼��ǰ��״̬
	private int mState;
	// ���ڸı��ͷ�ķ���Ķ���
	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	// ��������ʱ��
	private final int ROTATE_ANIM_DURATION = 180;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		mState = STATE_NORMAL;
		// ��ʼ����£���������ˢ��view�߶�Ϊ0
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		container = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(container, lp);
		// ��ʼ���ؼ�
		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);
		// ��ʼ������
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	// ����header��״̬
	public void setState(int state) {
		if (state == mState)
			return;

		// ��ʾ���
		if (state == STATE_REFRESHING) {
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			// ��ʾ��ͷ
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(HINT_NORMAL);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(HINT_READY);
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(HINT_LOADING);
			break;
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) container
				.getLayoutParams();
		lp.height = height;
		container.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return container.getHeight();
	}

	public void show() {
		container.setVisibility(View.VISIBLE);
	}

	public void hide() {
		container.setVisibility(View.INVISIBLE);
	}

}
