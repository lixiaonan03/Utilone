package com.lxn.utilone.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lxn.utilone.AppManager;


/**
 * activity的基类  
 * @author lxn
 */
public class BaseActivity extends Activity{

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
	    
		super.onResume();
		//overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		//overridePendingTransition(R.anim.out_to_left, R.anim.out_to_right); 
		//overridePendingTransition(R.anim.out_to_right, R.anim.out_to_left); 
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	
}
