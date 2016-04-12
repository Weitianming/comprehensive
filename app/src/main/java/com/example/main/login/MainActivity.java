package com.example.main.login;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.main.login.Mainfragment.MainView1;
import com.example.main.login.Mainfragment.MainView2;
import com.example.main.login.Mainfragment.MainView3;
import com.example.main.login.Mainfragment.MainView4;
import com.example.main.login.Mainfragment.MainView5;
import com.example.main.login.ibtn.IBtnCallListener;
import com.example.main.login.server.ChatService;
import com.example.main.login.view.DragLayout;
import com.example.main.login.view.DragLayout.DragListener;

public class MainActivity extends FragmentActivity implements IBtnCallListener {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment mainView1, mainView2, mainView3, mainView4, mainView5;
    private Button main_news, main_contact, main_dynamic, main_Photograph,
            main_Map;

    private DragLayout dl;
    private ImageView main_picture;
    private TextView main_show;
    private ListView main_list;
    private MyAdapter adapter;
    private String[] strings = new String[]{"我的QQ会员", "QQ钱包", "网上营业厅",
            "个性装扮", "我的收藏", "我的相册", "我的文件"};
    public static String name;

    public static SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        sp = getSharedPreferences("info", MODE_PRIVATE);
        name = sp.getString("name", "");

        new ChatService(this).start();

//		startService(new Intent(MainActivity.this, SearchService.class));

        initview();
        initDragLayout();
    }

    // 拖动布局
    public void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.d1);
        main_picture = (ImageView) findViewById(R.id.main_picture);
        main_show = (TextView) findViewById(R.id.main_show);
        main_list = (ListView) findViewById(R.id.main_list);

        Bitmap Resources = BitmapFactory.decodeResource(getResources(),
                R.drawable.p6_1);
        Bitmap bitmap = setCircular(Resources, 200);
        main_picture.setImageBitmap(bitmap);

        main_show.setText("最后之作御坂御坂");
        main_show.setTextSize(20);
        main_show.setTextColor(Color.parseColor("#ffffff"));

        // main_list.setAdapter(new ArrayAdapter<String>(MainActivity.this,
        // R.layout.main_item_text, new String[] { "我的QQ会员", "QQ钱包",
        // "网上营业厅", "个性装扮", "我的收藏", "我的相册", "我的文件" }));

        adapter = new MyAdapter();
        main_list.setAdapter(adapter);

        dl.setDragListener(new DragListener() {

            @Override
            public void onOpen() {
            }

            @Override
            public void onDrag(float percent) {
            }

            @Override
            public void onClose() {
            }
        });

    }

    // 初始化控件
    public void initview() {
        main_news = (Button) findViewById(R.id.main_news);
        main_contact = (Button) findViewById(R.id.main_contact);
        main_dynamic = (Button) findViewById(R.id.main_dynamic);
        main_Photograph = (Button) findViewById(R.id.main_Photograph);
        main_Map = (Button) findViewById(R.id.main_Map);
        main_news.setOnClickListener(new ButtonOnClickListener());
        main_contact.setOnClickListener(new ButtonOnClickListener());
        main_dynamic.setOnClickListener(new ButtonOnClickListener());
        main_Photograph.setOnClickListener(new ButtonOnClickListener());
        main_Map.setOnClickListener(new ButtonOnClickListener());

        fm = getSupportFragmentManager();
        mainView1 = new MainView1();
        mainView2 = new MainView2();
        mainView3 = new MainView3();
        mainView4 = new MainView4();
        mainView5 = new MainView5();

        ft = fm.beginTransaction();
        ft.add(R.id.main_container, mainView1).commit();

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return strings.length;
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
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.main_item_text, null);

            TextView main_list_item = (TextView) convertView
                    .findViewById(R.id.main_list_item);

            main_list_item.setText(strings[position]);

            return convertView;
        }

    }

    // 文字单击事件
    class ButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            ft = fm.beginTransaction();

            switch (v.getId()) {
                case R.id.main_news:
                    ft.replace(R.id.main_container, mainView1);
                    ft.commit();
                    main_news.setTextColor(Color.parseColor("#00a0eb"));
                    main_contact.setTextColor(Color.parseColor("#6f6f6f"));
                    main_dynamic.setTextColor(Color.parseColor("#6f6f6f"));
                    main_Photograph.setTextColor(Color.parseColor("#6f6f6f"));
                    main_Map.setTextColor(Color.parseColor("#6f6f6f"));
                    break;

                case R.id.main_contact:
                    ft.replace(R.id.main_container, mainView2);
                    ft.commit();
                    main_news.setTextColor(Color.parseColor("#6f6f6f"));
                    main_contact.setTextColor(Color.parseColor("#00a0eb"));
                    main_dynamic.setTextColor(Color.parseColor("#6f6f6f"));
                    main_Photograph.setTextColor(Color.parseColor("#6f6f6f"));
                    main_Map.setTextColor(Color.parseColor("#6f6f6f"));
                    break;

                case R.id.main_dynamic:
                    ft.replace(R.id.main_container, mainView3);
                    ft.commit();
                    main_news.setTextColor(Color.parseColor("#6f6f6f"));
                    main_contact.setTextColor(Color.parseColor("#6f6f6f"));
                    main_dynamic.setTextColor(Color.parseColor("#00a0eb"));
                    main_Photograph.setTextColor(Color.parseColor("#6f6f6f"));
                    main_Map.setTextColor(Color.parseColor("#6f6f6f"));
                    break;

                case R.id.main_Photograph:
                    ft.replace(R.id.main_container, mainView4);
                    ft.commit();
                    main_news.setTextColor(Color.parseColor("#6f6f6f"));
                    main_contact.setTextColor(Color.parseColor("#6f6f6f"));
                    main_dynamic.setTextColor(Color.parseColor("#6f6f6f"));
                    main_Photograph.setTextColor(Color.parseColor("#00a0eb"));
                    main_Map.setTextColor(Color.parseColor("#6f6f6f"));
                    break;

                case R.id.main_Map:
                    ft.replace(R.id.main_container, mainView5);
                    ft.commit();
                    main_news.setTextColor(Color.parseColor("#6f6f6f"));
                    main_contact.setTextColor(Color.parseColor("#6f6f6f"));
                    main_dynamic.setTextColor(Color.parseColor("#6f6f6f"));
                    main_Photograph.setTextColor(Color.parseColor("#6f6f6f"));
                    main_Map.setTextColor(Color.parseColor("#00a0eb"));
                    break;
            }
        }

    }

    @Override
    public void trans() {
        dl.open();
    }

    // 设置圆形头像
    public static Bitmap setCircular(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
