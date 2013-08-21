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
		case R.id.g_ppoz:
		case R.id.c_ppoz:
		case R.id.b_ppoz:
			Intent intent = new Intent(MainActivity.this, RemakeList.class);
			startActivity(intent);
			break;
		case R.id.t_gallery:
			String targetDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ppoz";
			Uri targetUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			targetUri = targetUri.buildUpon().appendQueryParameter("bucketId", String.valueOf(targetDir.toLowerCase().hashCode())).build();
			Intent intent2 = new Intent(Intent.ACTION_VIEW, targetUri);
			startActivity(intent2);
		}
	}
}