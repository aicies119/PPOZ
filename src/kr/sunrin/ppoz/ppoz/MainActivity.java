package kr.sunrin.ppoz.ppoz;

import android.app.*;
import android.content.*;
import android.os.*;
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
		case R.id.g_ppoz:
		case R.id.c_ppoz:
		case R.id.b_ppoz:
			Intent intent = new Intent(this, RemakeList.class);
			startActivity(intent);
			break;
		}
	}
}