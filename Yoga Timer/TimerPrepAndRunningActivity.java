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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class TimerPrepAndRunningActivity extends ListActivity {

	public static final String IS_RUNNING_KEY = "IS_RUNNING_KEY";

	public static final String PAUSE_TIME_KEY = "PAUSE_TIME_KEY";

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
	 * This button starts the current timer
	 */
	private Button startButton;

	private Button resumeButton;

	private Button cancelButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean isRunning = isRunning();

		if (isRunning) {
			setContentView(R.layout.running_timer_layout);

		} else {
			setContentView(R.layout.timer_prep_layout);
			setTimerPrepButtonViews();

		}
	}

	public void onResume() {
		super.onResume();
		boolean isRunning = isRunning();

		if (isRunning) {
			setContentView(R.layout.running_timer_layout);

			setRunningButtonViews();
		} else {
			setContentView(R.layout.timer_prep_layout);

			setTimerPrepButtonViews();

		}

	}

	/**
	 * This method sets all the listeners for the buttons involved in the timer
	 * adding view
	 */
	private void setTimerPrepButtonViews() {
		startButton = (Button) findViewById(R.id.start_button);

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
			}

		});

		// same as above
		drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				getListView().setVisibility(View.VISIBLE);
				startButton.setEnabled(true);

			}
		});
	}

	private void setRunningButtonViews() {
		resumeButton = (Button) findViewById(R.id.resume_button);
		cancelButton = (Button) findViewById(R.id.cancel_button);

		resumeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(TimerPrepAndRunningActivity.this,
						TimerActivity.class);
				startActivity(newIntent);

			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences settings = getSharedPreferences(
						MainMenuActivity.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean(TimerPrepAndRunningActivity.IS_RUNNING_KEY,
						false);
				editor.commit();
				finish();

			}
		});

	}

	@Override
	public void onBackPressed() {
		if (isRunning()) {
			return;
		}
		super.onBackPressed();
	}

	private boolean isRunning() {
		SharedPreferences settings = getSharedPreferences(
				MainMenuActivity.PREFS_NAME, 0);
		boolean isRunning = settings.getBoolean(IS_RUNNING_KEY, false);
		return isRunning;
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

		// If there are no exercises, don't start timer.
		if (exerciseList.isEmpty()) {
			return;
		}

		Intent newIntent = new Intent(TimerPrepAndRunningActivity.this,
				TimerActivity.class);
		newIntent.putExtra("thelist", exerciseList);
		startActivity(newIntent);

	}
}