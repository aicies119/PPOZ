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
	boolean focus, isFocus;
	ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		View vw = new View(this);
		Window win = getWindow();
		super.onCreate(savedInstanceState);
		win.setContentView(R.layout.activity_camera);
		
		//���̵� ���� ���̾� ����
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.camera_guide, null);
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		
		img = new ImageView(this);
		img.setImageResource(R.drawable.p_guide_1);
		LinearLayout.LayoutParams paramImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		paramImage.setMargins(0, 0, (int)pxFromDp(100), 0);
		linear.addView(img, paramImage);
		
		win.addContentView(linear, paramlinear);

		mShutter = (Button)findViewById(R.id.shutter);
		mSurface = (MyCameraSurface)findViewById(R.id.preview);
		
		/*mShutter.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				mSurface.mCamera.autoFocus(mAutoFocus);
				return false;
			}
		});*/
		
		img.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mSurface.mCamera.autoFocus(mAutoFocus);
				return true;
			}
		});
		
		//���� �Կ�
		mShutter.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!focus) {
					mSurface.mCamera.autoFocus(mAutoFocus);
					Handler handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							mSurface.mCamera.takePicture(null, null, mPicture);
						}
					};
					handler.sendEmptyMessageDelayed(0, 1500);
				}
				else {
					mSurface.mCamera.takePicture(null, null, mPicture);
				}
			}
		});
	}
	
	private float pxFromDp(float dp)
	{
	    return dp * context.getResources().getDisplayMetrics().density;
	}
	
	//��Ŀ�� �����ϸ� �Կ� �㰡
	AutoFocusCallback mAutoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			isFocus = success;
			focus = true;
		}
	};
	
	//���� ����
	PictureCallback mPicture = new PictureCallback() {
		StringBuilder name = new StringBuilder();
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i("PictureTaken", "Picture Taking!");
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
				Toast.makeText(CameraActivity.this, "���� ���� �� ���� �߻� : " + e.getMessage(), 0).show();
				return;
			}
			
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.parse("file://" + path);
			intent.setData(uri);
			sendBroadcast(intent);
			
			Toast.makeText(CameraActivity.this, "���� ���� �Ϸ� : " + path, 0).show();
			
			SharedPreferences.Editor edit = f_name.edit();
			
			cnt++;
			edit.putString("Date", date);
			edit.putInt("Num", cnt);
			
			edit.commit();
			name.setLength(0);
			
			focus = isFocus = false;
			
			mSurface.mCamera.startPreview();
		}
	};
}
