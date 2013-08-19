package kr.sunrin.ppoz.ppoz;

import java.io.*;

import android.app.*;
import android.content.*;
import android.content.IntentSender.*;
import android.content.pm.*;
import android.content.pm.PackageManager.*;
import android.content.res.*;
import android.content.res.Resources.*;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;

public class SplashActivity extends Activity {
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		initialize();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_BACK:
				onDestroy();
			}
		}
		return true;
	}
	
	private void initialize() {
		final Intent intent = new Intent(this, MainActivity.class);
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				//SharedPreferences px_pre = getSharedPreferences("px", 0);
				//SharedPreferences.Editor px_edit = px_pre.edit();
				//int px = (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 534, context.getResources().getDisplayMetrics()));
				
				//px_edit.putInt("px", px);
				//px_edit.commit();
				
				startActivity(intent);
				finish();
			}
		};
		handler.sendEmptyMessageDelayed(0, 2000);
	}
}
