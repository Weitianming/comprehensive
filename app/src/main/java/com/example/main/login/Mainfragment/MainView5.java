package com.example.main.login.Mainfragment;

import com.example.login.R;
import com.example.main.MainActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainView5 extends Fragment {
	private ImageView head;
	private Button PersonalData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.main_view5, container, false);

		head = (ImageView) rootView.findViewById(R.id.Head);
		PersonalData = (Button) rootView.findViewById(R.id.PersonalData);
		
		PersonalData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new MainActivity().sp.edit().putBoolean("Automatic", false).commit();
				startActivity(new Intent("com.example.login.LoginActivity"));
				getActivity().finish();
				
			}
		});
		
		
		
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		Bitmap Resources = BitmapFactory.decodeResource(getResources(),
				R.drawable.head);
		Bitmap bitmap = setCircular(Resources, 200.0f);
		head.setImageBitmap(bitmap);
		
	}

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
