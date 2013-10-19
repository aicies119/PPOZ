package kr.sunrin.ppoz.ppoz;

import java.io.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class PreviewActivity extends Activity {
	byte[] data;
	Intent send_intent = new Intent();
	String path = "";
	final static int ACT_SHARE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		ImageView img = (ImageView) this.findViewById(R.id.pre_img);
		Intent intent = getIntent();

		data = intent.getByteArrayExtra("pic");
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

		img.setImageBitmap(bitmap);

		findViewById(R.id.pre_ok).setOnClickListener(
				new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						pictureSave(data);
						finish();
					}
				});
		findViewById(R.id.pre_cancel).setOnClickListener(
				new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		findViewById(R.id.pre_share).setOnClickListener(
				new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (path == "") {
							pictureSave(data);
						}
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_SEND_MULTIPLE);
						intent.putExtra(Intent.EXTRA_SUBJECT,
								"Here are some files");
						intent.setType("image/jpeg");

						ArrayList<Uri> files = new ArrayList<Uri>();

						File file = new File(path);
						Uri uri = Uri.fromFile(file);
						files.add(uri);

						intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
								files);
						startActivity(intent.createChooser(intent, "공유"));
					}
				});
	}

	public void pictureSave(byte[] data) {
		StringBuilder name = new StringBuilder();
		SharedPreferences f_name = getSharedPreferences("FileName", 0);
		String f_date = f_name.getString("Date", "");
		String date = "";
		int cnt = f_name.getInt("Num", 1);
		Calendar cal = new GregorianCalendar();

		date = String.format("%d-%d-%d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

		if (!date.equals(f_date)) {
			cnt = 1;
		}

		name.append(date);
		name.append(String.format("-%d", cnt));

		String sd = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/ppoz";
		path = sd + "/" + name + ".jpg";

		File fpath = new File(sd);
		if (!fpath.exists()) {
			fpath.mkdirs();
		}

		File file = new File(path);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Toast.makeText(PreviewActivity.this,
					"파일 저장 중 에러 발생 : " + e.getMessage(), 0).show();
			return;
		}

		SharedPreferences.Editor edit = f_name.edit();

		cnt++;
		edit.putString("Date", date);
		edit.putInt("Num", cnt);

		edit.commit();
		name.setLength(0);

		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.parse("file://" + path);
		intent.setData(uri);
		sendBroadcast(intent);

		Toast.makeText(PreviewActivity.this, "사진 저장 완료 : " + path, 0).show();

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(sd)));

		setResult(RESULT_OK, send_intent);
	}
}
