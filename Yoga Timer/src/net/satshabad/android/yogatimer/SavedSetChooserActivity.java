package net.satshabad.android.yogatimer;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SavedSetChooserActivity extends ListActivity{
	
	protected static final int DELETE_SET_ALERT_DIALOG = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.saved_set_chooser_layout);
		
		StorageManager storage = new StorageManager();
		
		ArrayList<String> nameList = storage.getSetKeysFromFile(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.set_row, nameList);
		setListAdapter(adapter);
		ListView listView = getListView();
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showDialog(DELETE_SET_ALERT_DIALOG);
				return false;
			}
		});
	}

}
