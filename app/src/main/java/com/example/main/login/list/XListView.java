package com.example.main.login.list;

import com.example.login.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class XListView extends ListView {  
	  
    private final static int SCROLLBACK_HEADER = 0;  
    private final static int SCROLLBACK_FOOTER = 1;  
    // ����ʱ��  
    private final static int SCROLL_DURATION = 400;  
    // ���ظ��ľ���  
    private final static int PULL_LOAD_MORE_DELTA = 100;  
    // ��������  
    private final static float OFFSET_RADIO = 2f;  
    // ��¼���µ��y���  
    private float lastY;  
    // �����ع�  
    private Scroller scroller;  
    private IXListViewListener mListViewListener;  
    private XListViewHeader headerView;  
    private RelativeLayout headerViewContent;  
    // header�ĸ߶�  
    private int headerHeight;  
    // �Ƿ��ܹ�ˢ��  
    private boolean enableRefresh = true;  
    // �Ƿ�����ˢ��  
    private boolean isRefreashing = false;  
    // footer  
    private XListViewFooter footerView;  
    // �Ƿ���Լ��ظ��  
    private boolean enableLoadMore;  
    // �Ƿ����ڼ���  
    private boolean isLoadingMore;  
    // �Ƿ�footer׼��״̬  
    private boolean isFooterAdd = false;  
    // total list items, used to detect is at the bottom of listview.  
    private int totalItemCount;  
    // ��¼�Ǵ�header����footer����  
    private int mScrollBack;  
  
    private static final String TAG = "XListView";  
  
    public XListView(Context context) {  
        super(context);  
        initView(context);  
    }  
  
    public XListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        initView(context);  
    }  
  
    public XListView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        initView(context);  
    }  
  
    private void initView(Context context) {  
  
        scroller = new Scroller(context, new DecelerateInterpolator());  
  
        headerView = new XListViewHeader(context);  
        footerView = new XListViewFooter(context);  
  
        headerViewContent = (RelativeLayout) headerView  
                .findViewById(R.id.xlistview_header_content);  
        headerView.getViewTreeObserver().addOnGlobalLayoutListener(  
                new OnGlobalLayoutListener() {  
                    @SuppressWarnings("deprecation")  
                    @Override  
                    public void onGlobalLayout() {  
                        headerHeight = headerViewContent.getHeight();  
                        getViewTreeObserver()  
                                .removeGlobalOnLayoutListener(this);  
                    }  
                });  
        addHeaderView(headerView);  
  
    }  
  
    @Override  
    public void setAdapter(ListAdapter adapter) {  
        // ȷ��footer�����Ӳ���ֻ���һ��  
        if (isFooterAdd == false) {  
            isFooterAdd = true;  
            addFooterView(footerView);  
        }  
        super.setAdapter(adapter);  
  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {  
  
        totalItemCount = getAdapter().getCount();  
        switch (ev.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            // ��¼���µ����  
            lastY = ev.getRawY();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            // �����ƶ�����  
            float deltaY = ev.getRawY() - lastY;  
            lastY = ev.getRawY();  
            // �ǵ�һ��ұ����Ѿ���ʾ������������  
            if (getFirstVisiblePosition() == 0  
                    && (headerView.getVisiableHeight() > 0 || deltaY > 0)) {  
                updateHeaderHeight(deltaY / OFFSET_RADIO);  
            } else if (getLastVisiblePosition() == totalItemCount - 1  
                    && (footerView.getBottomMargin() > 0 || deltaY < 0)) {  
                updateFooterHeight(-deltaY / OFFSET_RADIO);  
            }  
            break;  
  
        case MotionEvent.ACTION_UP:  
  
            if (getFirstVisiblePosition() == 0) {  
                if (enableRefresh  
                        && headerView.getVisiableHeight() > headerHeight) {  
                    isRefreashing = true;  
                    headerView.setState(XListViewHeader.STATE_REFRESHING);  
                    if (mListViewListener != null) {  
                        mListViewListener.onRefresh();  
                    }  
                }  
                resetHeaderHeight();  
            } else if (getLastVisiblePosition() == totalItemCount - 1) {  
                if (enableLoadMore  
                        && footerView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {  
                    startLoadMore();  
                }  
                resetFooterHeight();  
            }  
            break;  
        }  
        return super.onTouchEvent(ev);  
    }  
  
    @Override  
    public void computeScroll() {  
  
        // ����֮�����  
        if (scroller.computeScrollOffset()) {  
  
            if (mScrollBack == SCROLLBACK_HEADER) {  
                headerView.setVisiableHeight(scroller.getCurrY());  
            } else {  
                footerView.setBottomMargin(scroller.getCurrY());  
            }  
            postInvalidate();  
        }  
        super.computeScroll();  
  
    }  
  
    public void setPullRefreshEnable(boolean enable) {  
        enableRefresh = enable;  
  
        if (!enableRefresh) {  
            headerView.hide();  
        } else {  
            headerView.show();  
        }  
    }  
  
    public void setPullLoadEnable(boolean enable) {  
        enableLoadMore = enable;  
        if (!enableLoadMore) {  
            footerView.hide();  
            footerView.setOnClickListener(null);  
        } else {  
            isLoadingMore = false;  
            footerView.show();  
            footerView.setState(XListViewFooter.STATE_NORMAL);  
            footerView.setOnClickListener(new OnClickListener() {  
                @Override  
                public void onClick(View v) {  
                    startLoadMore();  
                }  
            });  
        }  
    }  
  
    public void stopRefresh() {  
        if (isRefreashing == true) {  
            isRefreashing = false;  
            resetHeaderHeight();  
        }  
    }  
  
    public void stopLoadMore() {  
        if (isLoadingMore == true) {  
            isLoadingMore = false;  
            footerView.setState(XListViewFooter.STATE_NORMAL);  
        }  
    }  
  
    private void updateHeaderHeight(float delta) {  
        headerView.setVisiableHeight((int) delta  
                + headerView.getVisiableHeight());  
        // δ����ˢ��״̬�����¼�ͷ  
        if (enableRefresh && !isRefreashing) {  
            if (headerView.getVisiableHeight() > headerHeight) {  
                headerView.setState(XListViewHeader.STATE_READY);  
            } else {  
                headerView.setState(XListViewHeader.STATE_NORMAL);  
            }  
        }  
  
    }  
  
    private void resetHeaderHeight() {  
        // ��ǰ�Ŀɼ�߶�  
        int height = headerView.getVisiableHeight();  
        // �������ˢ�²��Ҹ߶�û����ȫչʾ  
        if ((isRefreashing && height <= headerHeight) || (height == 0)) {  
            return;  
        }  
        // Ĭ�ϻ�ع���header��λ��  
        int finalHeight = 0;  
        // ���������ˢ��״̬����ع���header�ĸ߶�  
        if (isRefreashing && height > headerHeight) {  
            finalHeight = headerHeight;  
        }  
        mScrollBack = SCROLLBACK_HEADER;  
        // �ع���ָ��λ��  
        scroller.startScroll(0, height, 0, finalHeight - height,  
                SCROLL_DURATION);  
        // ����computeScroll  
        invalidate();  
    }  
  
    private void updateFooterHeight(float delta) {  
        int height = footerView.getBottomMargin() + (int) delta;  
        if (enableLoadMore && !isLoadingMore) {  
            if (height > PULL_LOAD_MORE_DELTA) {  
                footerView.setState(XListViewFooter.STATE_READY);  
            } else {  
                footerView.setState(XListViewFooter.STATE_NORMAL);  
            }  
        }  
        footerView.setBottomMargin(height);  
  
    }  
  
    private void resetFooterHeight() {  
        int bottomMargin = footerView.getBottomMargin();  
        if (bottomMargin > 0) {  
            mScrollBack = SCROLLBACK_FOOTER;  
            scroller.startScroll(0, bottomMargin, 0, -bottomMargin,  
                    SCROLL_DURATION);  
            invalidate();  
        }  
    }  
  
    private void startLoadMore() {  
        isLoadingMore = true;  
        footerView.setState(XListViewFooter.STATE_LOADING);  
        if (mListViewListener != null) {  
            mListViewListener.onLoadMore();  
        }  
    }  
  
    public void setXListViewListener(IXListViewListener l) {  
        mListViewListener = l;  
    }  
  
    public interface IXListViewListener {  
  
        public void onRefresh();  
  
        public void onLoadMore();  
    }  
}
