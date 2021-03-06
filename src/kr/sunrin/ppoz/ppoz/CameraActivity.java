package kr.sunrin.ppoz.ppoz;

import android.app.*;
import android.content.*;
import android.hardware.*;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.media.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class CameraActivity extends Activity {
	MyCameraSurface mSurface;
	Context context = this;
	Button mShutter, mGallary;
	int px;
	Intent intent;
	boolean focus, isFocus;
	byte[] mData;
	MoveObject mo;
	SoundPool soundPool;
	int shutterSound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window win = getWindow();
		super.onCreate(savedInstanceState);
		win.setContentView(R.layout.activity_camera);

		Intent list_pos = getIntent();
		int typeNo = list_pos.getIntExtra("type", -1);
		int itemNo = list_pos.getIntExtra("item", -1);

		// 가이드 영역 레이어 생성
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linear = (LinearLayout) inflater.inflate(
				R.layout.camera_guide, null);
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		// 가이드라인과 카메라 화면의 크기를 맞출 수치 구한다
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int ph = metrics.heightPixels;
		int pw = metrics.widthPixels;

		// 가이드 영역 이미지뷰 생성
		switch (typeNo) {
		case 0:
			switch (itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(ph, pw-140);
				break;
			case 1:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_2);
				mo.setSize(ph, pw-140);
				break;
			case 2:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_3);
				mo.setSize(ph, pw-140);
				break;
			case 3:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_4);
				mo.setSize(ph, pw-140);
				break;
			case 4:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_5);
				mo.setSize(ph, pw-140);
				break;
			case 5:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_6);
				mo.setSize(ph, pw-140);
				break;
			case 6:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_7);
				mo.setSize(ph, pw-140);
				break;
			case 7:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_8);
				mo.setSize(ph, pw-140);
				break;
			case 8 : 
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_9);
				mo.setSize(ph, pw-140);
				break;
			case 9 : 
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_10);
				mo.setSize(ph, pw-140);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(ph, pw-140);
			}
			break;
		case 1:
			switch (itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_1);
				mo.setSize(ph, pw-140);
				break;
			case 1:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_2);
				mo.setSize(ph, pw-140);
				break;
			case 2:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_3);
				mo.setSize(ph, pw-140);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_1);
				mo.setSize(ph, pw-140);
			}
			break;
		case 2:
			switch (itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_1);
				mo.setSize(ph, pw-140);
				break;
			case 1:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_2);
				mo.setSize(ph, pw-140);
				break;
			case 2:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_3);
				mo.setSize(ph, pw-140);
				break;
			case 3:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_4);
				mo.setSize(ph, pw-140);
				break;
			case 4:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_5);
				mo.setSize(ph, pw-140);
				break;
			case 5:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_6);
				mo.setSize(ph, pw-140);
				break;
			case 6:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_7);
				mo.setSize(ph, pw-140);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_1);
				mo.setSize(ph, pw-140);
			}
			break;
		case 3:
			switch (itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(ph, pw-140);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(ph, pw-140);
			}
			break;
		}

		LinearLayout.LayoutParams paramImage = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		paramImage.setMargins(0, 0, (int) pxFromDp(100), 0);

		// 가이드 레이어에 이미지뷰 싣고
		linear.addView(mo, paramImage);

		intent = new Intent(this, PreviewActivity.class);
		// 가이드 레이어를 뷰 위에 올린다.
		win.addContentView(linear, paramlinear);

		mShutter = (Button) findViewById(R.id.shutter);
		mGallary = (Button) findViewById(R.id.gallary);
		mSurface = (MyCameraSurface) findViewById(R.id.preview);

		// 미리보기 영역 터치 시 오토포커싱 적용
		/*
		 * img.setOnTouchListener(new View.OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * mSurface.mCamera.autoFocus(mAutoFocus); return false; } });
		 */

		// 셔터음 준비
		soundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
		shutterSound = soundPool.load(this, R.raw.camera_click, 0);
		// 사진 촬영
		mShutter.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!focus) {
					mSurface.mCamera.autoFocus(mAutoFocus);
					Handler handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							mSurface.mCamera.takePicture(null, null, mPicture);
							soundPool.play(shutterSound, 1f, 1f, 0, 0, 1);	// 셔터음 실행
							
						}
					};
					handler.sendEmptyMessageDelayed(0, 1500);
				} else {
					mSurface.mCamera.takePicture(null, null, mPicture);
				}
			}
		});
		mGallary.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String targetDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ppoz";
				Uri targetUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				targetUri = targetUri.buildUpon().appendQueryParameter("bucketId", String.valueOf(targetDir.toLowerCase().hashCode())).build();
				Intent intent2 = new Intent(Intent.ACTION_VIEW, targetUri);
				startActivity(intent2); 
			}
		});
		/*
		 * findViewById(R.id.album).setOnClickListener(new
		 * Button.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { String targetDir =
		 * Environment.getExternalStorageDirectory().getAbsolutePath() +
		 * "/ppoz"; Uri targetUri = Meore.Images.Media.EXTERNAL_CONTENT_URI;
		 * targetUri =
		 * targetdiaStUri.buildUpon().appendQueryParameter("bucketId",
		 * String.valueOf(targetDir.toLowerCase().hashCode())).build(); Intent
		 * intent2 = new Intent(Intent.ACTION_VIEW, targetUri);
		 * startActivity(intent2); } });
		 */
	}

	private float pxFromDp(float dp) {
		return dp * context.getResources().getDisplayMetrics().density;
	}

	public void mfocus() {
		mSurface.mCamera.autoFocus(mAutoFocus);
	}

	// 포커싱 성공하면 촬영 허가
	AutoFocusCallback mAutoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			isFocus = success;
			focus = true;
		}
	};

	// 사진 저장
	PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			mData = data;
			intent.putExtra("pic", mData);
			startActivityForResult(intent, 0);
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
}