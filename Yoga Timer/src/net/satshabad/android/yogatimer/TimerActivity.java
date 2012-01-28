package net.satshabad.android.yogatimer;

import java.util.ArrayList;
import java.util.Stack;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author satshabad
 * 
 */
public class TimerActivity extends ListActivity {

	/**
	 * A list to hold the exercises to be counted down
	 */
	private ArrayList<Exercise> exerciseList;

	/**
	 * The time displayed to the user in milliseconds
	 */
	private long timeOnClock;

	/**
	 * the exercise currently being counted down
	 */
	private Exercise currentExercise;

	/**
	 * The actual timer object
	 */
	private MyCountDownTimerWrapper countDownTimer;

	/**
	 * A stack containing the exercises that have already been counted down
	 */
	private Stack<Exercise> completedExerciseStack;

	/**
	 * A display showing the time left in the current exercise
	 */
	private TextView timeDisplay;

	/**
	 * A display containing the name of the current exercise
	 */
	private TextView currentExerciseName;

	/**
	 * A list display containing the {@link #exerciseList} objects left to be
	 * counted down
	 */
	private ListView theListView;

	/**
	 * A custom adapter to allow the exercises be displayed with the time
	 */
	private ExerciseAdapter adapter;

	/**
	 * A button allowing user to reset the entire set of exercises
	 */
	private Button resetFromStartButton;

	/**
	 * A button allowing the user to pause the current exercise
	 */
	private Button pauseAndStartButton;

	/**
	 * A button allowing user to reset the current exercise
	 */
	private Button resetExerciseButton;

	/**
	 * Key to grab the exercise stack from the preferences
	 */
	private final String EXERCISE_STACK = "EXERCISE_STACK";

	/**
	 * Key to grab the exercise list from the preferences
	 */
	private final String EXERCISE_LIST = "EXERCISE_LIST";

	private PowerManager.WakeLock wakelock;

	
	
	public void onStart() {
		super.onStart();
		Log.d(MainMenuActivity.LOG_TAG, "Timer/ onStart has been called");
	}

	public void onResume() {
		super.onResume();
		Log.d(MainMenuActivity.LOG_TAG, "Timer/ onResume has been called");
		unPauseTimer();
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.d(MainMenuActivity.LOG_TAG,
				"Timer/ onSaveInstanceState has been called");

		savedInstanceState.putSerializable(EXERCISE_STACK,
				completedExerciseStack);
		savedInstanceState.putSerializable(EXERCISE_LIST, exerciseList);


	}

	public void onPause() {
		Log.d(MainMenuActivity.LOG_TAG, "Timer/ onPaused has been called");
		super.onPause();
		wakelock.release();
		pauseTimer();
		
	}
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(MainMenuActivity.LOG_TAG, "Timer/ onCreate has been called");
		setContentView(R.layout.timer_layout);
		
		// let the whole application know that the timer is now running
		SharedPreferences settings = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(TimerPrepActivity.IS_RUNNING_KEY, true);
	    editor.commit();
		
	    //Prevent the screen from sleeping
	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    wakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
	    wakelock.acquire();
	    
		// create buttons and other views
		resetExerciseButton = (Button) findViewById(R.id.reset_exercise);
		resetFromStartButton = (Button) findViewById(R.id.reset_set_button);
		pauseAndStartButton = (Button) findViewById(R.id.pause_button);
		currentExerciseName = (TextView) findViewById(R.id.exercise_name);
		timeDisplay = (TextView) findViewById(R.id.time_display);

		countDownTimer = new MyCountDownTimerWrapper(this);

		if (savedInstanceState == null) {
			// get the list of exercises
			Intent thisIntent = getIntent();
			Bundle listBundle = thisIntent.getExtras();
			try {
				exerciseList = (ArrayList<Exercise>) listBundle
						.getSerializable("thelist");
			} catch (Exception e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Some thing went wrong when trying to get list");
			}
			completedExerciseStack = new Stack<Exercise>();
		} else {

			try {
				 exerciseList= (ArrayList<Exercise>) savedInstanceState
						.getSerializable(EXERCISE_STACK);
				 completedExerciseStack= (Stack<Exercise>) savedInstanceState
						.getSerializable(EXERCISE_LIST);
			} catch (Exception e) {

				Log.e(MainMenuActivity.LOG_TAG,
						"problem getting old list or stack", e);

			}

		}

		Log.e(MainMenuActivity.LOG_TAG, "here2");
		// display the list of exercises
		adapter = new ExerciseAdapter(this, exerciseList);
		setListAdapter(adapter);

		theListView = getListView();
		// when an exercise in the list is clicked, start from that exercise
		theListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				boolean wasPaused = countDownTimer.isPaused();
				
				countDownTimer.stopTimer();
				int p = position;
				completedExerciseStack.push(currentExercise);
				while (p != 0) {
					completedExerciseStack.push(exerciseList.remove(0));
					p--;
				}
				adapter.notifyDataSetChanged();
				currentExercise = exerciseList.remove(0);
				adapter.notifyDataSetChanged();
				startCountDown(currentExercise);
				
				if (wasPaused){
					prepAndPauseTimer();
				}
			}

		});

		// when this button is clicked, restart from the beginning of current
		// exercise
		resetExerciseButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				restartExercise();
			}

		});

		// when this button is clicked, restart from the beginning of all the
		// exercises
		resetFromStartButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				restartFromTop();
			}

		});

		// when this button is clicked call pause to pause
		pauseAndStartButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (countDownTimer.isPaused() && countDownTimer.isRunning()) {
					pauseAndStartButton.setText("Pause");
					unPauseTimer();
				} else if (!countDownTimer.isPaused()
						&& countDownTimer.isRunning()) {
					pauseAndStartButton.setText("Start");
					pauseTimer();
				} else if (!countDownTimer.isRunning()) {
					return;
				}
			}

		});

		// grab the first exercise
		currentExercise = exerciseList.remove(0);
		adapter.notifyDataSetChanged();

		// start with the first exercise
		startCountDown(currentExercise);
	}

	public void startCountDown(final Exercise ex) {

		// set the display to the name of the current exercise
		currentExerciseName.setText(ex.getName());

		if (!countDownTimer.isRunning()) {
			countDownTimer.startTimer(ex.getTime());
		} else {
			return;
		}
	}

	public void pauseTimer() {
		countDownTimer.pauseTimer();
	}

	public void unPauseTimer() {

		countDownTimer.unPauseTimer();
	}

	public void restartFromTop() {
		
		boolean wasPaused = countDownTimer.isPaused();
		
		countDownTimer.stopTimer();

		if (completedExerciseStack.isEmpty()) {
			restartExercise();
		} else {
			exerciseList.add(0, currentExercise);
			while (!completedExerciseStack.isEmpty()) {
				exerciseList.add(0, completedExerciseStack.pop());
			}

			adapter = new ExerciseAdapter(this, exerciseList);
			setListAdapter(adapter);
			currentExercise = exerciseList.remove(0);
			adapter.notifyDataSetChanged();
			startCountDown(currentExercise);
		}
		
		if (wasPaused){
			prepAndPauseTimer();
		}
	}

	public void restartExercise() {
		boolean wasPaused = countDownTimer.isPaused();
		countDownTimer.stopTimer();
		startCountDown(currentExercise);
		
		if (wasPaused){
			prepAndPauseTimer();
		}

	}
	
	public void timerFinish() {
		if (!(exerciseList.isEmpty())) {
			completedExerciseStack.push(currentExercise);
			timeOnClock = exerciseList.get(0).getTime();
		}

		timeDisplay.setText("00:00");
		// don't let user click these buttons while media player plays sound
		resetExerciseButton.setClickable(false);
		resetFromStartButton.setClickable(false);
		
		MediaPlayer player = MediaPlayer.create(TimerActivity.this,
				R.raw.jap_bell);
		player.start();
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			public void onCompletion(MediaPlayer arg0) {
				arg0.release();
				if (!(exerciseList.isEmpty())) {
					currentExercise = exerciseList.remove(0);
					adapter.notifyDataSetChanged();
					timeDisplay.setText(Exercise.timeToString(currentExercise.getTime()));
					startCountDown(currentExercise);
					countDownTimer.pauseTimer();
					
					pauseAndStartButton.setText("Start");
					
					
					
				} else{
					currentExerciseName.setText("All Finished");
					pauseAndStartButton.setText("Pause");
					// let the whole application know that the timer is now NOT running
				    SharedPreferences settings = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
				    SharedPreferences.Editor editor = settings.edit();
				    editor.putBoolean(TimerPrepActivity.IS_RUNNING_KEY, false);
				    editor.commit();
				    wakelock.release();
				}
				
				resetExerciseButton.setClickable(true);
				resetFromStartButton.setClickable(true);	
				
			}
		});

	}

	public void timerTick(long millisUntilFinished) {
		timeDisplay.setText(((Exercise.timeToString(millisUntilFinished))));
		timeOnClock = millisUntilFinished;

	}

	private void prepAndPauseTimer(){
		timeDisplay.setText(Exercise.timeToString(currentExercise.getTime()));
		countDownTimer.pauseTimer();
	}

}
