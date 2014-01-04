package kr.sunrin.ppoz.ppoz;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void buttonOnClick(View v){
		switch(v.getId()){
		case R.id.p_ppoz :
			Intent saPIntent = new Intent(MainActivity.this, PoseList_P.class);
			startActivity(saPIntent);
			break;
		case R.id.g_ppoz :
			Intent saGIntent = new Intent(MainActivity.this, PoseList_G.class);
			startActivity(saGIntent);
			break;
		case R.id.c_ppoz :
			Intent saCIntent = new Intent(MainActivity.this, PoseList_C.class);
			startActivity(saCIntent);
			break;
		case R.id.s_ppoz :
			Intent saSIntent = new Intent(MainActivity.this, PoseList_S.class);
			startActivity(saSIntent);
			break;
		}
	}
	
	
	
	// 혹시 몰라 남겨두는 메뉴키
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/
}
