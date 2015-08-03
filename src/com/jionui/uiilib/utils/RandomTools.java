package com.jionui.uiilib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTools {

	private static Random random=new Random();
	private static int SIZE;

	
	public static List<String> setRandomList(List<String> urls){
		SIZE=urls.size();
		changePositions(urls);
		return urls;
	}
	
	public static void changePositions(List<String> urls){
		for(int i=SIZE-1;i>0;i--){
			exchange(urls,random.nextInt(i+1),i);
		}
	}
	
	private static void exchange(List<String> urls,int p1,int p2){
		String temp=urls.get(p1);
		urls.set(p1, urls.get(p2));
		urls.set(p2, temp);
	}
	

	
}
