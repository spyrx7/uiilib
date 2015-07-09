package com.jionui.uiilib;

import java.util.ArrayList;
import java.util.List;

import com.jionui.uiilib.AdSlidShowView.urlBackcall;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		int[] resimg=new int[]{R.drawable.img01,R.drawable.img02,R.drawable.img03,R.drawable.img04,R.drawable.img05};
		
		List<ImageView> adimgs=new ArrayList<ImageView>();
		for(int i=0;i<resimg.length;i++){
			ImageView imgView=new ImageView(this);
			imgView.setBackgroundResource(resimg[i]);
			adimgs.add(imgView);
		}
		AdSlidShowView adSlidShowView1=(AdSlidShowView) findViewById(R.id.adSlidShowView1);
		adSlidShowView1.setAdImageList(adimgs);
		adSlidShowView1.setOnUrlBackCall(new urlBackcall() {
			@Override
			public void onUrlBackCall(int i) {
				Toast.makeText(getApplicationContext(), ""+i, 0).show();
			}
		}
		

		
		);
		
	}


}
