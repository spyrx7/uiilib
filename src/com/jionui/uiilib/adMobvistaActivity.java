package com.jionui.uiilib;

import com.mobvista.cloud.sdk.AdListener;
import com.mobvista.cloud.sdk.MobvistaAd;

import android.app.Activity;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class adMobvistaActivity  extends Activity{
   
	private MobvistaAd mMobAd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mMobAd=new MobvistaAd(this, "19123", "445e58a5b4f07be588ada5ded3f08bbf");
		//mMobAd = new MobvistaAd(this, "1", "8a74b3df992e13e1ef0f70d72a35c6d2");
	
		mMobAd.loadAd(this, new AdListener() {
			
		
			
			@Override
			public void onAdLoaded() {
				
				mMobAd.showAd(MobvistaAd.SCENARIO_SPLASH);
			}

			@Override
			public void onAdClick() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAdClose() {
				// TODO Auto-generated method stub
				finish();
			}

			@Override
			public void onAdFailToLoad() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAdShow() {
				// TODO Auto-generated method stub
				
			}
			
	
		}, MobvistaAd.SCENARIO_SPLASH);
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK==keyCode){
			mMobAd.loadAd(this, new AdListener() {
				
				@Override
				public void onAdShow() {
					// TODO Auto-generated method stub
					mMobAd.showAd(MobvistaAd.SCENARIO_EXIT);
				}
				
				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAdFailToLoad() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAdClose() {
					// TODO Auto-generated method stub
					finish();
				}
				
				@Override
				public void onAdClick() {
					// TODO Auto-generated method stub
					
				}
			}, MobvistaAd.SCENARIO_EXIT);
			Toast.makeText(this, "tuichule", Toast.LENGTH_LONG).show();
		}
		
		return false;
	}
	
	@Override
	public void onBackPressed() {
		if(mMobAd.showAd(MobvistaAd.SCENARIO_EXIT)){
            super.onBackPressed();
        }
	}
}
