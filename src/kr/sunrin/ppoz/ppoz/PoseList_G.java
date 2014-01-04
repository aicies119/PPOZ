package kr.sunrin.ppoz.ppoz;

import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class PoseList_G extends Activity {

	private ArrayList<ListBox> arlb; // 리스트 저장용 어댑터
	private PopupWindow popup; // 팝업윈도우
	private View popupview; // 팝업윈도우를 띄우는 뷰
	private LinearLayout linear; // 팝업용 매개변수
	private ImageView popupimg; // 팝업창에 띄울 이미지
	private Boolean openpop=false; // 팝업창 상태 확인
	private int pos; // position 값을 받는다
	private int displayWidth, displayHeight; // 화면 비율 맞추는용도
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pose_list);
		
		// 화면의 크기를 구해준다.
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		displayWidth = dm.widthPixels;
		displayHeight = dm.heightPixels;
				
		// 리스트를 채우는 소스
		arlb = new ArrayList<ListBox>();
		ListBox lb;
		lb = new ListBox(R.drawable.g_guide_1, "5기통 춤");
		arlb.add(lb);
		lb = new ListBox(R.drawable.g_guide_2, "피라미드");
		arlb.add(lb);
		lb = new ListBox(R.drawable.g_guide_3, "크레용팝");
		arlb.add(lb);

		// 그리드뷰에 어댑터를 채워 넣음
		ListAdapter gridAdapter = new ListAdapter(this, R.layout.listbutton,
				arlb);

		GridView pose_list = (GridView) findViewById(R.id.poselist);
		pose_list.setAdapter(gridAdapter);

		// 팝업창을 띄우기위한 준비
		linear = (LinearLayout) findViewById(R.id.layout_pose_list);
		popupview = View.inflate(this, R.layout.popup, null);
		if(displayWidth<=480)
			popup = new PopupWindow(popupview, (int)displayWidth, displayHeight/2);
		else
			popup = new PopupWindow(popupview, (int)(displayWidth*0.7), displayHeight/3);
		popupimg = (ImageView) popupview.findViewById(R.id.popupimage);

		// 원하는 포즈 클릭시 팝업창 생성
		pose_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				popupimg.setImageResource(arlb.get(position).pose);
				popup.showAtLocation(linear, Gravity.CENTER, 0, 0);
				openpop = true;
			}
		});
		
		// 취소 버튼을 누를 시 팝업창 종료
		Button dismissPopup = (Button)popupview.findViewById(R.id.popupcancel);
		dismissPopup.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				popup.dismiss();	// 팝업창 종료
				openpop = false;
			}
		});
		
		// 원하는 포즈로 카메라 실행
		Button stCamera = (Button)popupview.findViewById(R.id.popupok);
		stCamera.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent stCIntent = new Intent(PoseList_G.this, CameraActivity.class);
				stCIntent.putExtra("type", 1);
				stCIntent.putExtra("item", pos);
				startActivity(stCIntent);
			}
			
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		if(keyCode == KeyEvent.KEYCODE_BACK){
			// 팝업창이 열려 있을 때 Back 키로 종료
			if(openpop){
				popup.dismiss();
				openpop = false;
			}
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		popup.dismiss();	// 팝업창 종료
		openpop = false;
	}

	// 포즈 이름과 이미지가 들어갈 틀
	class ListBox {
		ListBox(int pose, String name) {
			this.pose = pose;
			this.name = name;
		}

		int pose;
		String name;
	}

	class ListAdapter extends BaseAdapter {
		Context context;
		LayoutInflater Inflater;
		ArrayList<ListBox> arlb;
		int layout;

		public ListAdapter(Context context, int listbutton,
				ArrayList<ListBox> arlb) {
			this.context = context;
			Inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.arlb = arlb;
			layout = listbutton;
		}

		@Override
		public int getCount() { // 반환 값만큼의 갯수만 GridView로 들어가게 된다.
			return arlb.size();
		}

		@Override
		public String getItem(int position) {
			return arlb.get(position).name;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos = position;
			if (convertView == null) {
				convertView = Inflater.inflate(layout, parent, false);
				convertView
						.setLayoutParams(new GridView.LayoutParams(displayWidth/3, displayHeight/4));
			}

			TextView txt = (TextView) convertView.findViewById(R.id.posename);
			txt.setText(arlb.get(pos).name);

			ImageView img = (ImageView) convertView
					.findViewById(R.id.poseimage);
			img.setImageResource(arlb.get(pos).pose);

			return convertView;
		}

	}

}
