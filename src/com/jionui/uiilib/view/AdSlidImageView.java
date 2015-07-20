package com.jionui.uiilib.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jionui.uiilib.R;
import com.jionui.uiilib.utils.AnimUtils;

public class AdSlidImageView extends FrameLayout {

	private String tag = "AdSlidImageView";
	private List<ImageView> Adviews;
	private LinearLayout dotList;
	private LinearLayout adImages;
	private Context mcontext;
	private float x1 = 0;
	private float x2 = 0;
	private float y1 = 0;
	private float y2 = 0;
	private int currentItem = 0;
	private int windowX = 0, windowY = 0;

	public AdSlidImageView(Context context) {
		super(context);
		this.mcontext = context;
	}

	public AdSlidImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
		this.mcontext = context;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				currentItem--;
				if (currentItem < 0) {
					currentItem = adImages.getChildCount() - 1;
				} else if (currentItem > adImages.getChildCount() - 1) {
					currentItem = 0;
				}

			} else if (msg.what == 1) {
				currentItem++;
				if (currentItem < 0) {
					currentItem = adImages.getChildCount() - 1;
				} else if (currentItem > adImages.getChildCount() - 1) {
					currentItem = 0;
				}
			}

			animManger();
		};
	};

	public void setAdImageUrl(List<String> urls) {
		adImages.removeAllViews();

		for (int i = 0; i < urls.size(); i++) {
			ImageView img = new ImageView(mcontext);
			img.setMinimumWidth(windowX);
			img.setMinimumHeight(windowY);
			img.setMaxWidth(windowX);
			img.setMaxHeight(windowY);
		}
	}

	public void initView(Context context, AttributeSet attrs) {
		Adviews = new ArrayList<ImageView>();

		LayoutInflater.from(context).inflate(R.layout.ad_image_slid, this);
		adImages = (LinearLayout) findViewById(R.id.ll_imgageView);
		adImages.setBackgroundColor(Color.RED);
		dotList = (LinearLayout) findViewById(R.id.ll_dotparent);
		adImages.setOnTouchListener(new AdaTouchListener());

		WindowManager managet = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		managet.getDefaultDisplay().getMetrics(dm);
		windowX = dm.widthPixels;
		windowY = dm.heightPixels;

		int[] res = new int[] { R.drawable.img01, R.drawable.img02,
				R.drawable.img03 };
		for (int i = 0; i < res.length; i++) {
			ImageView img = new ImageView(context);
			img.setMinimumWidth(windowX);
			img.setMinimumHeight(windowY);
			img.setMaxWidth(windowX);
			img.setMaxHeight(windowY);

			img.setBackgroundResource(res[i]);
			adImages.addView(img);
		}

		Log.e(tag, "initView " + currentItem);

	}

	public class AdaTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {

			Log.e(tag, "isation");

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// 当手指按下的时候
				x1 = event.getX();
				y1 = event.getY();
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE){
				
				x2 = event.getX();
				y2 = event.getY();
			}
			if (event.getAction() == MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_OUTSIDE) {
				
				if (y1 - y2 > 50) {
					// 向上滑
				} else if (y2 - y1 > 50) {
					// 向下滑
				} else if (x1 - x2 > 50) {
					// 向左滑
						Message msg = handler.obtainMessage(1);
						handler.sendMessage(msg);
					
				} else if (x2 - x1 > 50) {
					// 向右滑
						Message msg = handler.obtainMessage(0);
						handler.sendMessage(msg);
				}

			}
			return false;
		}

	}

	private void animManger() {
		Log.e(tag, "animManger:" + currentItem);
		for (int i = 0; i < adImages.getChildCount(); i++) {
			if (currentItem == i) {
				ImageView view = (ImageView) adImages.getChildAt(i);
				view.setVisibility(View.VISIBLE);
			 view.setAnimation(AnimUtils.setTranslateAnimation(0, windowX,
				 0, 0, 500));
				Log.e(tag, "animManger: VISIBLE" + i);
			} else {
				ImageView view = (ImageView) adImages.getChildAt(i);
				view.setVisibility(View.GONE);
				Log.e(tag, "animManger GONE:" + i);
			}
		}

	}

}
