package com.jionui.uiilib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullToRefreshList extends LinearLayout {

	private View header;
	
	private ListView listView;
	
	private ProgressBar progressBar;
	
	private TextView description;
	
	private TextView updateAt;
	
	private ImageView arrow;
	
	
	
	public PullToRefreshList(Context context) {
		super(context);
	}

	public PullToRefreshList(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

}
