package net.satshabad.android.yogatimer;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class SavedSetChooserActivity extends ListActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.saved_set_chooser_layout);
		
		StorageManager storage = new StorageManager();
		
		ArrayList<String> nameList = storage.getSetKeysFromFile(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.set_row, nameList);
		setListAdapter(adapter);
	}

}
