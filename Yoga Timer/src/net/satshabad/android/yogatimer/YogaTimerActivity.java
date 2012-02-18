package net.satshabad.android.yogatimer;

import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class YogaTimerActivity extends Activity {
    
    /**
     * Tag for the LogCat logs for this app
     */
    public static final String LOG_TAG = "Yoga Timer";
    
	/**
	 * 
	 */
	public static final String IS_RUNNING_PREF_KEY = "IS_RUNNING_KEY";
	
	/**
	 * The list of set names that act as keys to get the files
	 */
	public static ArrayList<String> setKeys;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // this button leads to the timerPrep activity
        Button newSet = (Button)findViewById(R.id.the_new_button);
        
        StorageManager manager = new StorageManager();
        manager.putKeysToFile(new ArrayList<String>(), this);
        manager = null;
        
        //on the click the timerPrep activity will launch
        newSet.setOnClickListener(new OnClickListener()
      {
         @Override
         public void onClick(View arg0)
         {
            Log.d(LOG_TAG, "YogaTimerActivity/ButtonClick/launchTimer");
            launchTimer(); 
         }
      });
  }
    
    /**
     * Launches the TimerPrep activity
     * @pre true
     * @post the new activity is launched
     */
    private void launchTimer(){
       Log.d(LOG_TAG, "YogaTimerActivity/ActivityStart/TimerPrep");
       startActivity(new Intent(this, TimerPrepActivity.class));
       
    }
}