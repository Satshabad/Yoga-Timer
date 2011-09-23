package net.satshabad.android.yogatimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class YogaTimerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button newSet = (Button)findViewById(R.id.the_new_button);
        
        newSet.setOnClickListener(new OnClickListener()
      {
         
         @Override
         public void onClick(View arg0)
         {
            launchTimer();
            
         }
      });
  }

    public void launchTimer(){
       startActivity(new Intent(this, TimerPrep.class));
       
    }
}