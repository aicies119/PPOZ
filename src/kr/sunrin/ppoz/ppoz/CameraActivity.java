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

		// ���̵� ���� ���̾� ����
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linear = (LinearLayout) inflater.inflate(
				R.layout.camera_guide, null);
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		// ���̵���ΰ� ī�޶� ȭ���� ũ�⸦ ���� ��ġ ���Ѵ�
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// ���̵� ���� �̹����� ����
		switch (typeNo) {
		case 0:
			switch (itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 1:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_2);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 2:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_3);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 3:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_4);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 4:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_5);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 5:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_6);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 6:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_7);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 7:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_8);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
			}
			break;
		case 1:
			switch (itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 1:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_2);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 2:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_3);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 3:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_4);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 4:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_5);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 5:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_6);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
			}
			break;
		case 2:
			switch (itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 1:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_2);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			case 2:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_3);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.c_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
			}
			break;
		case 3:
			switch (itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels-140);
			}
			break;
		}

		LinearLayout.LayoutParams paramImage = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		paramImage.setMargins(0, 0, (int) pxFromDp(100), 0);

		// ���̵� ���̾ �̹����� �ư�
		linear.addView(mo, paramImage);

		intent = new Intent(this, PreviewActivity.class);
		// ���̵� ���̾ �� ���� �ø���.
		win.addContentView(linear, paramlinear);

		mShutter = (Button) findViewById(R.id.shutter);
		mGallary = (Button) findViewById(R.id.gallary);
		mSurface = (MyCameraSurface) findViewById(R.id.preview);

		// �̸����� ���� ��ġ �� ������Ŀ�� ����
		/*
		 * img.setOnTouchListener(new View.OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * mSurface.mCamera.autoFocus(mAutoFocus); return false; } });
		 */

		// ������ �غ�
		soundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
		shutterSound = soundPool.load(this, R.raw.camera_click, 0);
		// ���� �Կ�
		mShutter.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!focus) {
					mSurface.mCamera.autoFocus(mAutoFocus);
					Handler handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							mSurface.mCamera.takePicture(null, null, mPicture);
							soundPool.play(shutterSound, 1f, 1f, 0, 0, 1);	// ������ ����
							
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

	// ��Ŀ�� �����ϸ� �Կ� �㰡
	AutoFocusCallback mAutoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			isFocus = success;
			focus = true;
		}
	};

	// ���� ����
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
