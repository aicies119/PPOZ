package kr.sunrin.ppoz.ppoz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class CameraActivity extends Activity {
	MyCameraSurface mSurface;
	Context context = this;
	Button mShutter;
	int px;
	Intent intent;
	boolean focus, isFocus;
	byte[] mData;
	MoveObject mo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window win = getWindow();
		super.onCreate(savedInstanceState);
		win.setContentView(R.layout.activity_camera);
		
		Intent list_pos = getIntent();
		int typeNo = list_pos.getIntExtra("type", -1);
		int itemNo = list_pos.getIntExtra("item", -1);
		
		//���̵� ���� ���̾� ����
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.camera_guide, null);
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		
		// ���̵���ΰ� ī�޶� ȭ���� ũ�⸦ ���� ��ġ ���Ѵ�
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		//���̵� ���� �̹����� ����
		switch(typeNo) {
		case 0:
			switch(itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
			}
			break;
		case 1:
			switch(itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
				break;
			case 1:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.piramid);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.g_guide_1);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
			}
			break;
		case 2:
			switch(itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.das);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.das);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
			}
			break;
		case 3:
			switch(itemNo) {
			case 0:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_2);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
				break;
			default:
				mo = new MoveObject(this);
				mo.setResource(R.drawable.p_guide_2);
				mo.setSize(metrics.heightPixels, metrics.widthPixels);
			}
			break;
		}
		
		LinearLayout.LayoutParams paramImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		paramImage.setMargins(0, 0, (int)pxFromDp(100), 0);
		
		//���̵� ���̾ �̹����� �ư�
		linear.addView(mo, paramImage);
		
		intent = new Intent(this, PreviewActivity.class);
		//���̵� ���̾ �� ���� �ø���.
		win.addContentView(linear, paramlinear);

		mShutter = (Button)findViewById(R.id.shutter);
		mSurface = (MyCameraSurface)findViewById(R.id.preview);
		
		//�̸����� ���� ��ġ �� ������Ŀ�� ����
/*		img.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mSurface.mCamera.autoFocus(mAutoFocus);
				return false;
			}
		});
		*/
		
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
		
/*		findViewById(R.id.album).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String targetDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ppoz";
				Uri targetUri = Meore.Images.Media.EXTERNAL_CONTENT_URI;
				targetUri = targetdiaStUri.buildUpon().appendQueryParameter("bucketId", String.valueOf(targetDir.toLowerCase().hashCode())).build();
				Intent intent2 = new Intent(Intent.ACTION_VIEW, targetUri);
				startActivity(intent2);
			}
		});*/
	}

	private float pxFromDp(float dp)
	{
	    return dp * context.getResources().getDisplayMetrics().density;
	}
	
	public void mfocus(){
		mSurface.mCamera.autoFocus(mAutoFocus);
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
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			mData = data;
			intent.putExtra("pic", mData);
			startActivityForResult(intent, 0);
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode) {
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
