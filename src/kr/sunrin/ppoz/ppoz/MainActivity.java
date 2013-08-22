package kr.sunrin.ppoz.ppoz;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
	Button testBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void mainOnClick(View v) {
		switch(v.getId()) {
		case R.id.p_ppoz:
			Intent intent_p = new Intent(MainActivity.this, PoseList_P.class);
			startActivity(intent_p);
			break;
		case R.id.g_ppoz:
			Intent intent_g = new Intent(MainActivity.this, PoseList_G.class);
			startActivity(intent_g);
			break;
		case R.id.c_ppoz:
			Intent intent_b30 = new Intent(MainActivity.this, PoseList_B30.class);
			startActivity(intent_b30);
			break;
		case R.id.b_ppoz:
			Intent intent = new Intent(MainActivity.this, PoseList_S.class);
			startActivity(intent);
			break;
		}
	}
}