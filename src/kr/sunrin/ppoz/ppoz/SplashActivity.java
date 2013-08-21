package kr.sunrin.ppoz.ppoz;

import android.app.*;
import android.content.*;
import android.os.*;
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
				startActivity(intent);
				finish();
			}
		};
		handler.sendEmptyMessageDelayed(0, 2000);
	}
}
