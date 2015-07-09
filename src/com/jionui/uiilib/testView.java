package com.jionui.uiilib;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;

public class testView extends LinearLayout  {

	private String tag = "testView";
	private View v;

	private boolean isLoadLyOne = false;

	public testView(Context context) {
		super(context);

	}

	public testView(Context context, AttributeSet attrs) {
		super(context, attrs);

		v = new ImageView(context);
		v.setBackgroundColor(Color.RED);
		addView(v);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		v.layout(left, top, right/2, bottom/2);
		if (changed && !isLoadLyOne) {
			Log.i(tag, ">>>>>>>onLayout");
			isLoadLyOne = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(tag, ">>>>>>>onTouch ACTION_DOWN");
			Log.i(tag, ">>>>>>>onTouch Y="+event.getY());
			Log.i(tag, ">>>>>>>onTouch X="+event.getX());
			v.measure((int)event.getX(), (int)event.getY());
			break;
		case MotionEvent.ACTION_MASK:
			Log.i(tag, ">>>>>>>onTouch ACTION_MASK");
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i(tag, ">>>>>>>onTouch ACTION_MOVE");
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.i(tag, ">>>>>>>onTouch ACTION_CANCEL");
			break;
		case MotionEvent.ACTION_OUTSIDE:
			Log.i(tag, ">>>>>>>onTouch ACTION_OUTSIDE");
			break;
		case MotionEvent.ACTION_UP:
			Log.i(tag, ">>>>>>>onTouch ACTION_UP");
			break;
		case MotionEvent.ACTION_SCROLL:
			Log.i(tag, ">>>>>>>onTouch ACTION_SCROLL");
			break;
		case MotionEvent.ACTION_HOVER_ENTER:
			Log.i(tag, ">>>>>>>onTouch ACTION_HOVER_ENTER");
			break;
		case MotionEvent.ACTION_HOVER_EXIT:
			Log.i(tag, ">>>>>>>onTouch ACTION_HOVER_EXIT");
			break;

		default:
			break;
		}
		return false;
	}

}
