package com.lxn.utilone.tabfragmentdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTwo extends BaseFragment {

    public FragmentTwo() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("lixiaonan", "two=====onPause=");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("lixiaonan", "two=====onResume=");
	}
}
