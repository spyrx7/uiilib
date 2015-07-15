package com.jionui.uiilib;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import com.jionui.uiilib.view.AdSlidShowView;
import com.jionui.uiilib.view.AdSlidShowView.urlBackcall;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
		int[] resimg=new int[]{R.drawable.img01,R.drawable.img02,R.drawable.img03};
		String[] urlimg=new String[]{
				"http://d.hiphotos.baidu.com/image/pic/item/3812b31bb051f8192d0e6b30d9b44aed2e73e7a5.jpg"
				,"http://h.hiphotos.baidu.com/image/pic/item/eac4b74543a9822650cf435d8982b9014a90ebba.jpg"
				,"http://f.hiphotos.baidu.com/image/pic/item/d6ca7bcb0a46f21f5bfc3729f5246b600d33ae84.jpg"};
 	
		List<ImageView> adimgs=new ArrayList<ImageView>();
		for(int i=0;i<resimg.length;i++){
			Bitmap bit=getHttpBitmap(urlimg[i]);
			ImageView imgView=new ImageView(this);
			imgView.setImageBitmap(bit);
			adimgs.add(imgView);
		}
		AdSlidShowView adSlidShowView1=(AdSlidShowView) findViewById(R.id.adSlidShowView1);
		adSlidShowView1.setAdImageList(adimgs);
		//adSlidShowView1.setAdimgUrl(urlimg);
		adSlidShowView1.setOnUrlBackCall(new urlBackcall() {
			@Override
			public void onUrlBackCall(int i) {
				Toast.makeText(getApplicationContext(), ""+i, 0).show();
			}
		}
		);
	}
	
	 public static Bitmap getHttpBitmap(String url){
	        URL myFileURL;
	        Bitmap bitmap=null;
	        try{
	            myFileURL = new URL(url);
	            //�������
	            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
	            //���ó�ʱʱ��Ϊ6000���룬conn.setConnectionTiem(0);��ʾû��ʱ������
	            conn.setConnectTimeout(3000);
	            //�������û�������
	            conn.setDoInput(true);
	            //��ʹ�û���
	            conn.setUseCaches(false);
	            //�����п��ޣ�û��Ӱ��
	            //conn.connect();
	            //�õ������
	            InputStream is = conn.getInputStream();
	            //�����õ�ͼƬ
	            bitmap = BitmapFactory.decodeStream(is);
	            //�ر������
	            is.close();
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	         
	        return bitmap;
	         
	    }
}
