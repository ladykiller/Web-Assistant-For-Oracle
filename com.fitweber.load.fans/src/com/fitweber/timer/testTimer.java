package com.fitweber.timer;

import java.util.Date;
import java.util.TimerTask;

import com.fitweber.util.CommonUtils;

public class testTimer extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("当前时间为: "+CommonUtils.formatTime(new Date())+",注意休息！");
		
	}
}
