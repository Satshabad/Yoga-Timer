package net.satshabad.android.yogatimer;

import java.util.ArrayList;
import java.util.Stack;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This Activity manages a running list of timers that are set off in serial.
 * 
 * @author satshabad
 * 
 */
public class TimerActivity extends ListActivity {

	/**
	 * A list to hold the exercises to be counted down
	 */
	private ArrayList<Exercise> exerciseList;

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
	 * Manages storing and retrieving files
	 */
	private StorageManager storage;

	public void onPause() {
		super.onPause();
		if (!isRunning()) {
			pauseTimer();
			// notify the user that the timer was paused
			Toast.makeText(getApplicationContext(), "Timer Paused",
					Toast.LENGTH_LONG).show();
			exerciseList.add(0, currentExercise);
			// Store objects that will be retrieved when the activity resumes in
			// onResume
			storage = new StorageManager();
			storage.putRunningCountDownTimer(countDownTimer,
					this);
			storage.putRunningList(exerciseList, this);
			storage.putRunningStack(completedExerciseStack,
					this);
			storage = null;
		}
	}

	public void onResume() {
		super.onResume();
		boolean isRunning = isRunning();

		if (isRunning) {

			// Retrieve objects needed to continue the countdown timer
			storage = new StorageManager();
			exerciseList = storage.getRunningList(this);
			completedExerciseStack = storage.getRunningStack(this);
			countDownTimer = storage.getRunningCountDownTimer(this);

			// This activity may have been killed during the pause, so reset it.
			countDownTimer.setActivity(this);
			storage = null;
		} else {

			// If exerciseList was not retrieved from bundle or file, make a new
			// empty one to prevent crashing.
			if (exerciseList == null) {
				exerciseList = new ArrayList<Exercise>();
			}
			completedExerciseStack = new Stack<Exercise>();
			countDownTimer = new MyCountDownTimerWrapper(this);
		}
		// display the list of exercises
		adapter = new ExerciseAdapter(this, exerciseList);
		setListAdapter(adapter);

		// grab the first exercise
		currentExercise = exerciseList.remove(0);
		adapter.notifyDataSetChanged();

		if (isRunning) {
			// aready running? just unpause it.
			// also set title
			currentExerciseName.setText(currentExercise.getName());
			unPauseTimer();
		} else {
			// start with the first exercise
			startCountDown(currentExercise);
		}

	}

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(MainMenuActivity.LOG_TAG, "Timer/ onCreate has been called");
		setContentView(R.layout.timer_layout);

		// create buttons and other views
		resetExerciseButton = (Button) findViewById(R.id.reset_exercise);
		resetFromStartButton = (Button) findViewById(R.id.reset_set_button);
		pauseAndStartButton = (Button) findViewById(R.id.pause_button);
		currentExerciseName = (TextView) findViewById(R.id.exercise_name);
		timeDisplay = (TextView) findViewById(R.id.time_display);

		// Create object to control count down functions. i.e. pause, etc
		countDownTimer = new MyCountDownTimerWrapper(this);

		// This flag allows the activity to exist above the lock screen
		// needs to be replaced with something that works
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

		if (!isRunning()) {
			try {
				// get the list of exercises
				Intent thisIntent = getIntent();
				Bundle listBundle = thisIntent.getExtras();
				exerciseList = (ArrayList<Exercise>) listBundle
						.getSerializable("thelist");
			} catch (Exception e) {

			}
		}

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

				if (wasPaused) {
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
	}

	public void startCountDown(final Exercise ex) {

		// let the whole application know that the timer is now running
		SharedPreferences settings = getSharedPreferences(
				MainMenuActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(TimerPrepActivity.IS_RUNNING_KEY, true);
		editor.commit();

		// set the display to the name of the current exercise
		currentExerciseName.setText(ex.getName());

		if (!countDownTimer.isRunning()) {
			countDownTimer.startTimer(ex.getTime());
		} else {
			return;
		}
	}

	/**
	 * Pauses the current running timer
	 */
	public void pauseTimer() {
		countDownTimer.pauseTimer();
	}

	/**
	 * Unpauses the current running timer
	 */
	public void unPauseTimer() {

		countDownTimer.unPauseTimer();
	}

	/**
	 * Restarts the list of timers from the very beginning
	 */
	public void restartFromTop() {

		boolean wasPaused = countDownTimer.isPaused();
		
		countDownTimer.stopTimer();

		if (completedExerciseStack.isEmpty()) {
			restartExercise();
		} else {
			
			// Stuff all the exercises from the stack back into the list
			exerciseList.add(0, currentExercise);
			while (!completedExerciseStack.isEmpty()) {
				exerciseList.add(0, completedExerciseStack.pop());
			}
			
			// Set up the list again and restart timer
			adapter = new ExerciseAdapter(this, exerciseList);
			setListAdapter(adapter);
			currentExercise = exerciseList.remove(0);
			adapter.notifyDataSetChanged();
			startCountDown(currentExercise);
		}

		// if it was paused, keep it paused
		if (wasPaused) {
			prepAndPauseTimer();
		}
	}

	/**
	 * Restarts the current timer form the beginning
	 */
	public void restartExercise() {
		boolean wasPaused = countDownTimer.isPaused();
		countDownTimer.stopTimer();
		startCountDown(currentExercise);

		// if it was paused, keep it paused
		if (wasPaused) {
			prepAndPauseTimer();
		}

	}

	/**
	 * Called when the timer finishes
	 */
	public void timerFinish() {
		if (!(exerciseList.isEmpty())) {
			completedExerciseStack.push(currentExercise);
		}
		
		// Otherwise will display 00:01
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
					timeDisplay.setText(Exercise.timeToString(currentExercise
							.getTime()));
					startCountDown(currentExercise);
					countDownTimer.pauseTimer();

					pauseAndStartButton.setText("Start");

				} else {
					currentExerciseName.setText("All Finished");
					pauseAndStartButton.setText("Pause");
					// let the whole application know that the timer is now NOT
					// running
					SharedPreferences settings = getSharedPreferences(
							MainMenuActivity.PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean(TimerPrepActivity.IS_RUNNING_KEY, false);
					editor.commit();

				}

				// Let the user click these buttons now that they do something
				resetExerciseButton.setClickable(true);
				resetFromStartButton.setClickable(true);

			}
		});

	}

	/**
	 * Called when the timer ticks and updates the display
	 * @param millisUntilFinished 
	 */
	public void timerTick(long millisUntilFinished) {
		timeDisplay.setText(((Exercise.timeToString(millisUntilFinished))));

	}

	/**
	 * Sets text to exercise name and pauses timer
	 */
	private void prepAndPauseTimer() {
		timeDisplay.setText(Exercise.timeToString(currentExercise.getTime()));
		countDownTimer.pauseTimer();
	}

	/**
	 * Finds out if there is a running timer or not
	 * 
	 * @return true or false
	 */
	private boolean isRunning() {
		SharedPreferences settings = getSharedPreferences(
				MainMenuActivity.PREFS_NAME, 0);
		return settings.getBoolean(MainMenuActivity.IS_RUNNING_PREF_KEY, false);
	}

}
