package net.satshabad.android.yogatimer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

public class TimerPrep extends ListActivity {

    /**
     * A container for the timer setting mechanism
     */
    private SlidingDrawer drawer;

    /**
     * A list holding the exercises entered by the user
     */
    private ArrayList<Exercise> exerciseList;

    /**
     * An adapter to hold the elements in {@link #exerciseList} and then display
     * them in a list
     */
    private ExerciseAdapter adapter;

    private TimerSetter theTimerSetter;

    /**
     * The index of the current exercise
     */
    private int exerciseIndex = 0;

    /**
     * A list of number in string format
     */
    String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_prep_layout);
        Log.d(YogaTimerActivity.LOG_TAG, "here1");
        
        theTimerSetter = new TimerSetter(this);
        ViewGroup container = (ViewGroup) findViewById(R.id.content);
        container.addView(theTimerSetter);      
        Log.d(YogaTimerActivity.LOG_TAG, "here2");
        exerciseList = new ArrayList<Exercise>();

        adapter = new ExerciseAdapter(this, exerciseList);
        setListAdapter(adapter);
        drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);

        theTimerSetter.setOnTimeSetListener(new OnTimeSetListener() {
            
            @Override
            public void onTimeSet(String exerciseName, long time) {
                 addExerciseToList(exerciseName, time);
            }
        });
    }

    protected void addExerciseToList(String exerciseName, long time) {
        exerciseList.add(exerciseIndex, new Exercise(exerciseName, time));
        exerciseIndex++;
        theTimerSetter.setNextTimerName("Exercise " + exerciseIndex);
        adapter.notifyDataSetChanged();
        drawer.close();
        
    }

    
    public void startTimer(View v) {
        Intent newIntent = new Intent(TimerPrep.this, Timer.class);
        newIntent.putExtra("thelist", exerciseList);
        startActivity(newIntent);

    }
}
