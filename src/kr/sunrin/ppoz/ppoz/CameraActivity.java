package kr.sunrin.ppoz.ppoz;

import java.io.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.hardware.*;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class CameraActivity extends Activity {
	MyCameraSurface mSurface;
	Context context = this;
	Button mShutter;
	int px;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window win = getWindow();
		super.onCreate(savedInstanceState);
		win.setContentView(R.layout.activity_camera);
		
		//SharedPreferences px_pre = getSharedPreferences("px", 0);
		//px = px_pre.getInt("px", 0);
		
		
		//가이드 영역 레이어 생성
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.camera_guide, null);
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		
		ImageView img = new ImageView(this);
		img.setImageResource(R.drawable.p_guide_1);
		LinearLayout.LayoutParams paramImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		paramImage.setMargins(0, 0, (int)pxFromDp(100), 0);
		linear.addView(img, paramImage);
		
		win.addContentView(linear, paramlinear);
		
		mSurface = (MyCameraSurface)findViewById(R.id.preview);
		
		//오토 포커스 시작
		findViewById(R.id.focus).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mShutter.setEnabled(false);
				mSurface.mCamera.autoFocus(mAutoFocus);
			}
		});
		
		//사진 촬영
		mShutter = (Button)findViewById(R.id.shutter);
		mShutter.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSurface.mCamera.takePicture(null, null, mPicture);
			}
		});
	}
	
	private float pxFromDp(float dp)
	{
	    return dp * context.getResources().getDisplayMetrics().density;
	}
	
	//포커싱 성공하면 촬영 허가
	AutoFocusCallback mAutoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			mShutter.setEnabled(success);
		}
	};
	
	//사진 저장
	PictureCallback mPicture = new PictureCallback() {
		StringBuilder name = new StringBuilder();
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			SharedPreferences f_name = getSharedPreferences ("FileName", 0);
			String f_date = f_name.getString("Date", "");
			String date = "";
			int cnt = f_name.getInt("Num", 1);
			Calendar cal = new GregorianCalendar();
			
			date = String.format("%d-%d-%d", cal.get(Calendar.YEAR), 
					cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
			
			if(!date.equals(f_date)) {
				cnt = 1;
			}
			
			name.append(date);
			name.append(String.format("-%d", cnt));
			
			String sd = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ppoz";
			String path = sd + "/" + name + ".jpg";
			
			File fpath = new File(sd);
			if(!fpath.exists()) {
				fpath.mkdirs();
			}
			
			File file = new File(path);
			
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data);
				fos.flush();
				fos.close();
			} catch(Exception e) {
				Toast.makeText(CameraActivity.this, "파일 저장 중 에러 발생 : " + e.getMessage(), 0).show();
				return;
			}
			
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.parse("file://" + path);
			intent.setData(uri);
			sendBroadcast(intent);
			
			Toast.makeText(CameraActivity.this, "사진 저장 완료 : " + path, 0).show();
			
			SharedPreferences.Editor edit = f_name.edit();
			
			cnt++;
			edit.putString("Date", date);
			edit.putInt("Num", cnt);
			
			edit.commit();
			name.setLength(0);
			
			
			mSurface.mCamera.startPreview();
		}
	};
}
