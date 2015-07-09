package com.jionui.uiilib;

import java.util.ArrayList;
import java.util.List;




import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AdSlidShowView extends FrameLayout {
	private String tag="AdSlidShowView";
    private android.support.v4.view.ViewPager viewPageAd;
    private LinearLayout dotList;
    private View view;
    private Context context;
    
    private List<ImageView> adlistImage;
    private String[] imgUrl;
    
    private int currentItem=0;
	public urlBackcall adBackCall;
    public interface urlBackcall{
    	void onUrlBackCall(int i);
    }
    
    public void setOnUrlBackCall(urlBackcall callBack){
    	adBackCall=callBack;
    }
    
    
	public AdSlidShowView(Context context) {
		super(context);
		this.context=context;
		initView(context,null);
	}

	public AdSlidShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    this.context=context;
		initView(context,attrs);
		
	}

	public void setAdImageList(List<ImageView> adlistImage){
		dotList.removeAllViews();
		this.adlistImage=adlistImage;
		for(int i=0;i<adlistImage.size();i++){
			View v=new View(context);
			if(i==0)
				v.setBackgroundResource(R.drawable.dian);
			else
				v.setBackgroundResource(R.drawable.dian_down);
			v.setLayoutParams(new LayoutParams(10, 10));
			dotList.addView(v);
		}
		
		Log.i(tag, "setAdImageList");
	}
	
	public void setAdimgUrl(String[] urls){
		this.imgUrl=urls;
	}
	
	private void initView(Context context, AttributeSet attrs) {
		
		TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.AdSlidShowView);
	    LayoutInflater.from(context).inflate(R.layout.ad_viewpage_view, this);
		
		int[] resimg=new int[]{R.drawable.img01,R.drawable.img02,R.drawable.img03,R.drawable.img04,R.drawable.img05};
		adlistImage=new ArrayList<ImageView>();
		
		for(int i=0;i<resimg.length;i++){
			ImageView imgView=new ImageView(context);
			imgView.setBackgroundResource(resimg[i]);
			adlistImage.add(imgView);
		}
		
		viewPageAd=(ViewPager) findViewById(R.id.ad_viewPager);
		viewPageAd.setFocusable(true);
		viewPageAd.setAdapter(new adviewPagerAdpter());
		viewPageAd.setOnPageChangeListener(new adviewpagerListener());
		dotList=(LinearLayout) findViewById(R.id.ll_dotparent);
		Log.i(tag, "initView");
		
	}
	

	
	
	
	private class adviewPagerAdpter extends PagerAdapter{
		@Override
		public int getCount() {	
			return adlistImage.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(adlistImage.get(position));
			
		}
		@Override
		public Object instantiateItem(View container, final int position) {
			((ViewPager)container).addView(adlistImage.get(position));
			
			ImageView img=adlistImage.get(position);
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					adBackCall.onUrlBackCall(position);
				}
			});
			
			return adlistImage.get(position);
		}
		
	}
	
	private class adviewpagerListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {
			//Log.i(tag, ""+arg0);
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			
		}

		@Override
		public void onPageSelected(int arg0) {
			Log.i(tag, ""+arg0);
			
			currentItem = arg0;
			for (int i = 0; i < dotList.getChildCount(); i++) {
				if (i == arg0) {
					((View) dotList.getChildAt(i))
							.setBackgroundResource(R.drawable.dian);
				} else {
					((View) dotList.getChildAt(i))
							.setBackgroundResource(R.drawable.dian_down);
				}
			}
			
			
		}
		
	}

}
