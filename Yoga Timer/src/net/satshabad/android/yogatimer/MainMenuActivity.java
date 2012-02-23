package net.satshabad.android.yogatimer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {
    
    /**
     * Tag for the LogCat logs for this app
     */
    public static final String LOG_TAG = "Yoga Timer";
    public static final String PREFS_NAME = "PREFS_NAME";
    
	public static final String IS_RUNNING_PREF_KEY = "IS_RUNNING_KEY";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(MainMenuActivity.IS_RUNNING_PREF_KEY, false);
	    editor.commit();
	     
        // this button leads to the timerPrep activity
        Button newSet = (Button)findViewById(R.id.the_new_button);
        
        Button savedSet = (Button) findViewById(R.id.the_saved_button);
        
        //on the click the timerPrep activity will launch
        newSet.setOnClickListener(new OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
        	v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Log.d(LOG_TAG, "YogaTimerActivity/ButtonClick/launchTimer");
            launchTimer(); 
         }
      });
        
        savedSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				launchChooser();
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
       startActivity(new Intent(this, TimerPrepAndRunningActivity.class));
       
    }
    
    private void launchChooser(){
    	startActivity(new Intent(this, SavedSetChooserActivity.class));
    }
}