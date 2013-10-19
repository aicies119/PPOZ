package kr.sunrin.ppoz.ppoz;

import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class PoseList_B30 extends Activity {
	ArrayList<Item> arlist; // 리스트 데이터
	PopupWindow popup; // 팝업윈도우
	View popupview; // 팝업윈도우에 띄우는 뷰
	LinearLayout linear; // 어디에 팝업윈도우를 띄울지 가르키는 뷰
	ImageView img; // 팝업뷰 이미지 전달용
	boolean openPop; // 팝업창 상태
	// Intent intent;
	int pos; // position 전달용

	class Item { // 리스트 데이터를 채우는 틀
		Item(int image, String name) {
			this.image = image;
			this.name = name;
		}

		int image; // 포즈 이미지
		String name; // 포즈 제목
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		// 리스트 데이터 채우기
		arlist = new ArrayList<Item>();
		Item item;
		item = new Item(R.drawable.c_guide_1, "1");
		arlist.add(item);
		item = new Item(R.drawable.c_guide_2, "2");
		arlist.add(item);
		item = new Item(R.drawable.c_guide_3, "3");
		arlist.add(item);

		// GridView 제작
		GridView poselist = (GridView) findViewById(R.id.poselist);
		ListAdapter listadapter = new ListAdapter(this, R.layout.listbutton,
				arlist);
		poselist.setAdapter(listadapter);

		// 팝업
		linear = (LinearLayout) findViewById(R.id.remake_list);
		popupview = View.inflate(this, R.layout.popup, null);
		popup = new PopupWindow(popupview, 480, 500);

		// 원하는 포즈 클릭시 팝업창 생성
		img = (ImageView) popupview.findViewById(R.id.popupimage); // OnItemClickListener 안에서 안되서 밖에서 처리
		poselist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				openPop = true;
				img.setImageResource(arlist.get(position).image);
				popup.showAtLocation(linear, Gravity.CENTER, 0, 0);
			}
		});

		Button submit = (Button) popupview.findViewById(R.id.popupok); // 팝업확인
																		// 버튼
		submit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				sub(pos);
			}
		});

		// 팝업윈도우 - 취소버튼을 누를시 팝업창 종료
		Button dismissPopup = (Button) popupview.findViewById(R.id.popupcancel);
		dismissPopup.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				popup.dismiss(); // 팝업창 종료
				openPop = false;
			}
		});
	}

	public void sub(int position) {
		Intent intent = new Intent(PoseList_B30.this, CameraActivity.class);
		intent.putExtra("type", 2);
		intent.putExtra("item", position); // CameraActivity.java에서 사용하기 위함
		startActivity(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		popup.dismiss(); // 팝업창 종료
		openPop = false; // 팝업창 open 태그 = false
	}

	// BACK키를 눌렀을 때 팝업창 종료
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (openPop) {
				popup.dismiss();
				openPop = false;
				return false;
			}
		}
		return true;
	}

	// GridView에 전달할 어뎁터
	class ListAdapter extends BaseAdapter {
		Context context;
		LayoutInflater Inflater;
		ArrayList<Item> arlist;
		int layout;

		// 생성자
		public ListAdapter(Context context, int layout, ArrayList<Item> arlist) {
			this.context = context;
			this.layout = layout;
			this.arlist = arlist;
			Inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // 뭔지는 모르지만 필요함
		}

		@Override
		public int getCount() {
			return arlist.size();
		}

		@Override
		public String getItem(int position) {
			return arlist.get(position).name;
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
						.setLayoutParams(new GridView.LayoutParams(200, 280)); // LayoutParams(200, 200)가 각 뷰의 크기
			}

			// 이미지
			ImageView img = (ImageView) convertView
					.findViewById(R.id.poseimage);
			img.setImageResource(arlist.get(pos).image);

			// 텍스트
			TextView txt = (TextView) convertView.findViewById(R.id.posename);
			txt.setText(arlist.get(pos).name);
			return convertView;
		}
	}
}
