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

	private ArrayList<ListBox> arlb; // ����Ʈ ����� �����
	private PopupWindow popup; // �˾�������
	private View popupview; // �˾������츦 ���� ��
	private LinearLayout linear; // �˾��� �Ű�����
	private ImageView popupimg; // �˾�â�� ��� �̹���
	private Boolean openpop=false; // �˾�â ���� Ȯ��
	private int pos; // position ���� �޴´�
	private int displayWidth, displayHeight; // ȭ�� ���� ���ߴ¿뵵
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pose_list);
		
		// ȭ���� ũ�⸦ �����ش�.
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		displayWidth = dm.widthPixels;
		displayHeight = dm.heightPixels;
				
		// ����Ʈ�� ä��� �ҽ�
		arlb = new ArrayList<ListBox>();
		ListBox lb;
		lb = new ListBox(R.drawable.g_guide_1, "5���� ��");
		arlb.add(lb);
		lb = new ListBox(R.drawable.g_guide_2, "�Ƕ�̵�");
		arlb.add(lb);
		lb = new ListBox(R.drawable.g_guide_3, "ũ������");
		arlb.add(lb);

		// �׸���信 ����͸� ä�� ����
		ListAdapter gridAdapter = new ListAdapter(this, R.layout.listbutton,
				arlb);

		GridView pose_list = (GridView) findViewById(R.id.poselist);
		pose_list.setAdapter(gridAdapter);

		// �˾�â�� �������� �غ�
		linear = (LinearLayout) findViewById(R.id.layout_pose_list);
		popupview = View.inflate(this, R.layout.popup, null);
		if(displayWidth<=480)
			popup = new PopupWindow(popupview, (int)displayWidth, displayHeight/2);
		else
			popup = new PopupWindow(popupview, (int)(displayWidth*0.7), displayHeight/3);
		popupimg = (ImageView) popupview.findViewById(R.id.popupimage);

		// ���ϴ� ���� Ŭ���� �˾�â ����
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
		
		// ��� ��ư�� ���� �� �˾�â ����
		Button dismissPopup = (Button)popupview.findViewById(R.id.popupcancel);
		dismissPopup.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				popup.dismiss();	// �˾�â ����
				openpop = false;
			}
		});
		
		// ���ϴ� ����� ī�޶� ����
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
			// �˾�â�� ���� ���� �� Back Ű�� ����
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
		popup.dismiss();	// �˾�â ����
		openpop = false;
	}

	// ���� �̸��� �̹����� �� Ʋ
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
		public int getCount() { // ��ȯ ����ŭ�� ������ GridView�� ���� �ȴ�.
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
