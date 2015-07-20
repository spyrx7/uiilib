package com.jionui.uiilib.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimUtils {
	
	public static Animation setAlphaMation(float fromAlpha,float toAlpha,int time){
		
		AlphaAnimation alphaanim=new AlphaAnimation(fromAlpha, toAlpha);
		alphaanim.setDuration(time);
		
		return alphaanim;
	}
	
	public static  Animation setRotateAnimation(float fromDegrees ,float toDegrees,float pivotX,float pivotY,int time){
		
		RotateAnimation rotateanim=new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		rotateanim.setDuration(time);
		
		return rotateanim;
		
	}
	
	public static Animation setScaleAnimation(float fromX,float toX,float fromY,float toY,int time){
		ScaleAnimation anim=new ScaleAnimation(fromX, toX, fromY, toY);
		anim.setDuration(time);
		return anim;
	}
	
	public static Animation setTranslateAnimation(float fromXDelta,float toXDelta,float fromYDelta,float toYDelta,int time){
		
		TranslateAnimation anim=new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		anim.setDuration(time);
		return anim;	
	}
	

}
