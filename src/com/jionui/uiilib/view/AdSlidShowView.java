package com.jionui.uiilib.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jionui.uiilib.R;
import com.jionui.uiilib.utils.ImageDownLoader;
import com.jionui.uiilib.utils.RandomTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AdSlidShowView extends FrameLayout {
	private String tag = "AdSlidShowView";
	private android.support.v4.view.ViewPager viewPageAd;
	private LinearLayout dotList;
	private Context context;

	private List<ImageView> adlistImage;
	private int currentItem = 0;
	public urlBackcall adBackCall;
	protected int lastPosition;

	public interface urlBackcall {
		void onUrlBackCall(int i);
	}

	public void setOnUrlBackCall(urlBackcall callBack) {
		adBackCall = callBack;
	}

	public AdSlidShowView(Context context) {
		super(context);
		this.context = context;
		initView(context, null);
	}

	public AdSlidShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context, attrs);
		initViewPager();
		
	}

	/**
	 * ʹ�� List<ImageView> �����ֲ�
	 * 
	 * @param adlistImage
	 */
	public void setAdImageList(List<ImageView> adlistImage) {
		dotList.removeAllViews();
		this.adlistImage = adlistImage;
		setDotLists(this.adlistImage);
	}



	/**
	 * 默认 5个 轮播
	 * 
	 * 1 使用setAdImageList 设置 List<ImageView> imgs
	 * 
	 * 2 使用getAdListImage 设置 List<String> Urls
	 * 
	 * @param context
	 * @param attrs
	 */
	private void initView(Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.ad_viewpage_view, this);

		int[] resimg = new int[] { R.drawable.img01, R.drawable.img01,
				R.drawable.img01, R.drawable.img01, R.drawable.img01 };
		adlistImage = new ArrayList<ImageView>();

		for (int i = 0; i < resimg.length; i++) {
			ImageView imgView = new ImageView(context);
			imgView.setBackgroundResource(resimg[i]);
			adlistImage.add(imgView);
		}

	}

	private void initViewPager() {
		viewPageAd = (ViewPager) findViewById(R.id.ad_viewPager);
		viewPageAd.setFocusable(true);
		viewPageAd.setAdapter(new adviewPagerAdpter());
		viewPageAd.setCurrentItem(Integer.MAX_VALUE/2 - (Integer.MAX_VALUE/2%adlistImage.size())) ;
		viewPageAd.setOnPageChangeListener(new adviewpagerListener());
		dotList = (LinearLayout) findViewById(R.id.ll_dotparent);
	}

	private class adviewPagerAdpter extends PagerAdapter {
		@Override
		public int getCount() {
			// Log.i(tag, "adviewPagerAdpter" + adlistImage.size());
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			//((ViewPager) container).removeView(adlistImage.get(position));
			((ViewPager) container).removeView((View) object);
			object = null;
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			((ViewGroup) container).addView(adlistImage.get(position%adlistImage.size()));
			
			ImageView img = adlistImage.get(position%adlistImage.size());
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					adBackCall.onUrlBackCall(position%adlistImage.size());
				}
			});

			return adlistImage.get(position%adlistImage.size());
		}

	}

	private class adviewpagerListener implements OnPageChangeListener {
		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
			switch (arg0) {
			case 1:
				isAutoPlay = false;
				
				break;
			case 2:
				isAutoPlay = true;
				break;
			case 0:
				if (viewPageAd.getCurrentItem() == viewPageAd.getAdapter()
						.getCount() - 1 && !isAutoPlay) {

					viewPageAd.setCurrentItem(0);
				} else if (viewPageAd.getCurrentItem() == 0 && !isAutoPlay) {
					viewPageAd.setCurrentItem(viewPageAd.getAdapter()
							.getCount() - 1);
				}

				break;

			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			// Log.i(tag, "" + arg0);
			
			position = position%adlistImage.size();
			for (int i = 0; i < dotList.getChildCount(); i++) {
				if (i == position) {
					((View) dotList.getChildAt(i))
							.setBackgroundResource(R.drawable.dian);
				} else {
					((View) dotList.getChildAt(i))
							.setBackgroundResource(R.drawable.dian_down);
				}
			}
			

		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	

	public void setImagesFromUrl(List<String> urls) {
		//Log.d("BS", "setImageFromUrl:" + urls.toString());
		dotList.removeAllViews();	
		this.adlistImage = getAdListImage(urls);
		setDotLists(this.adlistImage);
		ImageDownLoader loader = new ImageDownLoader(this.getContext());
		urls=RandomTools.setRandomList(urls);
		Log.d(tag, urls.toString());
		Log.d(tag, urls.size()+"");
		for (int i = 0; i < urls.size(); i++) {
			String url = urls.get(i);
			final int index = i;
			Bitmap bitmap = loader.getBitmapCache(url);
			if (bitmap != null) {
				setImage(bitmap, i);
			} else {
				if (loader.getTaskCollection().containsKey(url)) {
					return;
				}

				loader.loadImage(url, this.getWidth(), this.getHeight(),
						new ImageDownLoader.AsyncImageLoaderListener() {

							@Override
							public void onImageLoader(Bitmap bitmap) {
								if (this != null && bitmap != null) {
									setImage(bitmap, index);
									// Log.e("�e�`��", index+"");
								}
							}

						});
			}
		}
	}

	/**
	 * ʹ�� List<String> urls �����ֲ�
	 * 
	 * @param urls
	 *            ���Ӽ���
	 * @return
	 */
	private List<ImageView> getAdListImage(List<String> urls) {
		List<ImageView> imgs = new ArrayList<ImageView>();
		for (int i = 0; i < urls.size(); i++) {
			ImageView view = new ImageView(context);
			view.setBackgroundResource(R.drawable.img01);
			imgs.add(view);
		}
		return imgs;
	}

	public void setDotLists(List<ImageView> adlistImage) {
		for (int i = 0; i < adlistImage.size(); i++) {
			View v = new View(context);
			if (i == 0)
				v.setBackgroundResource(R.drawable.dian);
			else
				v.setBackgroundResource(R.drawable.dian_down);
			v.setLayoutParams(new LayoutParams(10, 10));
			dotList.addView(v);
		}
		initViewPager();
	}

	public void setImage(Bitmap bitmap, int i) {
		ImageView view = adlistImage.get(i);
		Drawable drawable = new BitmapDrawable(bitmap);
		view.setBackgroundDrawable(drawable);
		requestLayout();
		invalidate();
	}

}
