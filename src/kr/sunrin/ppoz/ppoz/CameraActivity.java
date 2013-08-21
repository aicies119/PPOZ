package kr.sunrin.ppoz.ppoz;

import android.app.*;
import android.content.*;
import android.hardware.*;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.*;
import android.view.*;
import android.widget.*;

public class CameraActivity extends Activity {
	MyCameraSurface mSurface;
	Context context = this;
	Button mShutter;
	int px;
	Intent intent;
	boolean focus, isFocus;
	byte[] mData;
	ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window win = getWindow();
		super.onCreate(savedInstanceState);
		win.setContentView(R.layout.activity_camera);
		
		Intent list_pos = getIntent();
		int itemNo = list_pos.getIntExtra("item", -1);
		
		//���̵� ���� ���̾� ����
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.camera_guide, null);
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

		//���̵� ���� �̹����� ����
		switch(itemNo) {
		case 1:
			img = new ImageView(this);
			img.setImageResource(R.drawable.p_guide_2);
			break;
		case 2:
			img = new ImageView(this);
			img.setImageResource(R.drawable.c_guide_1);
			break;
		case 3:
			img = new ImageView(this);
			img.setImageResource(R.drawable.g_guide_1);
			break;
		default:
			img = new ImageView(this);
			img.setImageResource(R.drawable.p_guide_1);
		}
		
		LinearLayout.LayoutParams paramImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		paramImage.setMargins(0, 0, (int)pxFromDp(100), 0);
		
		//���̵� ���̾ �̹����� �ư�
		linear.addView(img, paramImage);
		
		intent = new Intent(this, PreviewActivity.class);
		//���̵� ���̾ �� ���� �ø���.
		win.addContentView(linear, paramlinear);

		mShutter = (Button)findViewById(R.id.shutter);
		mSurface = (MyCameraSurface)findViewById(R.id.preview);
		
		//�̸����� ���� ��ġ �� ������Ŀ�� ����
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
