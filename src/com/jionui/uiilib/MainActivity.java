package com.jionui.uiilib;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.jionui.uiilib.view.AdSlidShowView;
import com.jionui.uiilib.view.AdSlidShowView.urlBackcall;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List<String> urls=new ArrayList<String>();
		urls.add("http://www.cheney2000.cn/pic/nb2hi4b2f4xws3lbm5sxglrzhfzgkylefzrw63jpnzsxoudsn5shky3uf42domjpgyydalrwgayc6yjqgaydamzyg4ztmnjonjygo.jpg");
		urls.add("http://a2.att.hudong.com/74/65/20300001316158131494651920873.jpg");
		urls.add("http://pic.baike.soso.com/p/20130617/20130617092843-1583169869.jpg");
		urls.add("http://pic.baike.soso.com/p/20130617/bki-20130617092748-1986008497.jpg");
		urls.add("http://www.cheney2000.cn/pic/nb2hi4b2f4xxsltiovxxg2dfnyxg4zluf5zxiylunfrs66liovqv64djmmxtglzqf43s6mzqg42tinjngy3wkyzwgu4wcn3egnqtsobvgnswkyrvgfsdknzzgfqtmmlcgazs42tqm4xfelrwgayc42tqm4.jpg");
		urls.add("http://pic.baike.soso.com/p/20120618/20120618214950-2135345509.jpg");
		
		
		AdSlidShowView adview=(AdSlidShowView) findViewById(R.id.adSlidShowView1);
		adview.setImagesFromUrl(urls);
		adview.setOnUrlBackCall(new urlBackcall() {
			
			@Override
			public void onUrlBackCall(int i) {
				
				Toast.makeText(MainActivity.this, ""+i, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
}
