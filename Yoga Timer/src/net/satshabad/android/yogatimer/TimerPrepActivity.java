package net.satshabad.android.yogatimer;

import java.util.ArrayList;

import net.satshabad.android.timersetter.OnTimeSetListener;
import net.satshabad.android.timersetter.TimerSetter;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;



public class TimerPrepActivity extends ListActivity {

	public static final String IS_RUNNING_KEY = "IS_RUNNING_KEY";
	
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
	 * This button allows the user to save the created set for later use
	 */
	private Button saveButton;

	/**
	 * This button starts the current timer
	 */
	private Button startButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
	    boolean isRunning = settings.getBoolean(IS_RUNNING_KEY, false);
		
	    if(isRunning){
	    	setContentView(R.layout.running_timer_layout);
	    	
	    	
	    }else{
			setContentView(R.layout.timer_prep_layout);

			startButton = (Button) findViewById(R.id.start_button);
			saveButton = (Button) findViewById(R.id.save_button);

			// using the custom timer setter view
			theTimerSetter = (TimerSetter) findViewById(R.id.timer);
			exerciseList = new ArrayList<Exercise>();

			adapter = new ExerciseAdapter(this, exerciseList);
			setListAdapter(adapter);
			drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);

			theTimerSetter.setNextTimerName("Exercise " + (exerciseIndex + 1));

			// when the user sets the timer, call the addExerciseToList method with
			// the time and name
			theTimerSetter.setOnTimeSetListener(new OnTimeSetListener() {

				@Override
				public void onTimeSet(String exerciseName, long time) {
					addExerciseToList(exerciseName, time);
				}
			});

			// this eliminates a bug that sometimes allows the user to click "under"
			// the timer setting drawer
			drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

				@Override
				public void onDrawerOpened() {
					getListView().setVisibility(View.INVISIBLE);
					startButton.setEnabled(false);
					saveButton.setEnabled(false);
				}

			});

			// same as above
			drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

				@Override
				public void onDrawerClosed() {
					getListView().setVisibility(View.VISIBLE);
					startButton.setEnabled(true);
					saveButton.setEnabled(true);

				}
			});

	    }
	}

	public void onResume(){
		super.onResume();
	
		
	}
	
	protected void addExerciseToList(String exerciseName, long time) {
		exerciseList.add(exerciseIndex, new Exercise(exerciseName, time));
		exerciseIndex++;
		theTimerSetter.setNextTimerName("Exercise " + (exerciseIndex + 1));
		adapter.notifyDataSetChanged();
		drawer.close();

	}

	public void startTimer(View v) {
		v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		Intent newIntent = new Intent(TimerPrepActivity.this,
				TimerActivity.class);
		newIntent.putExtra("thelist", exerciseList);
		startActivity(newIntent);

	}
}
