package kr.sunrin.ppoz.ppoz;

import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class PoseList_B30 extends Activity {
	ArrayList<Item> arlist; // ����Ʈ ������
	PopupWindow popup; // �˾�������
	View popupview; // �˾������쿡 ���� ��
	LinearLayout linear; // ��� �˾������츦 ����� ����Ű�� ��
	ImageView img; // �˾��� �̹��� ���޿�
	boolean openPop; // �˾�â ����
	// Intent intent;
	int pos; // position ���޿�

	class Item { // ����Ʈ �����͸� ä��� Ʋ
		Item(int image, String name) {
			this.image = image;
			this.name = name;
		}

		int image; // ���� �̹���
		String name; // ���� ����
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		// ����Ʈ ������ ä���
		arlist = new ArrayList<Item>();
		Item item;
		item = new Item(R.drawable.c_guide_1, "1");
		arlist.add(item);
		item = new Item(R.drawable.c_guide_2, "2");
		arlist.add(item);
		item = new Item(R.drawable.c_guide_3, "3");
		arlist.add(item);

		// GridView ����
		GridView poselist = (GridView) findViewById(R.id.poselist);
		ListAdapter listadapter = new ListAdapter(this, R.layout.listbutton,
				arlist);
		poselist.setAdapter(listadapter);

		// �˾�
		linear = (LinearLayout) findViewById(R.id.remake_list);
		popupview = View.inflate(this, R.layout.popup, null);
		popup = new PopupWindow(popupview, 480, 500);

		// ���ϴ� ���� Ŭ���� �˾�â ����
		img = (ImageView) popupview.findViewById(R.id.popupimage); // OnItemClickListener �ȿ��� �ȵǼ� �ۿ��� ó��
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

		Button submit = (Button) popupview.findViewById(R.id.popupok); // �˾�Ȯ��
																		// ��ư
		submit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				sub(pos);
			}
		});

		// �˾������� - ��ҹ�ư�� ������ �˾�â ����
		Button dismissPopup = (Button) popupview.findViewById(R.id.popupcancel);
		dismissPopup.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				popup.dismiss(); // �˾�â ����
				openPop = false;
			}
		});
	}

	public void sub(int position) {
		Intent intent = new Intent(PoseList_B30.this, CameraActivity.class);
		intent.putExtra("type", 2);
		intent.putExtra("item", position); // CameraActivity.java���� ����ϱ� ����
		startActivity(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		popup.dismiss(); // �˾�â ����
		openPop = false; // �˾�â open �±� = false
	}

	// BACKŰ�� ������ �� �˾�â ����
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

	// GridView�� ������ ���
	class ListAdapter extends BaseAdapter {
		Context context;
		LayoutInflater Inflater;
		ArrayList<Item> arlist;
		int layout;

		// ������
		public ListAdapter(Context context, int layout, ArrayList<Item> arlist) {
			this.context = context;
			this.layout = layout;
			this.arlist = arlist;
			Inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // ������ ������ �ʿ���
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
						.setLayoutParams(new GridView.LayoutParams(200, 280)); // LayoutParams(200, 200)�� �� ���� ũ��
			}

			// �̹���
			ImageView img = (ImageView) convertView
					.findViewById(R.id.poseimage);
			img.setImageResource(arlist.get(pos).image);

			// �ؽ�Ʈ
			TextView txt = (TextView) convertView.findViewById(R.id.posename);
			txt.setText(arlist.get(pos).name);
			return convertView;
		}
	}
}
