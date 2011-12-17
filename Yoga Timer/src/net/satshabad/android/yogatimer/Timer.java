package net.satshabad.android.yogatimer;

import java.util.ArrayList;
import java.util.Stack;

import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Timer extends ListActivity {

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
    private MyCounter theCountDown;

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
    private Button resetFromStart;

    /**
     * A button allowing the user to pause the current exercise
     */
    private Button pause;

    /**
     * A button allowing user to reset the current exercise
     */
    private Button resetExercise;

    
    private final String EXERCISE_STACK = "EXERCISE_STACK";
    
    private final String EXERCISE_LIST = "EXERCISE_LIST";

    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(YogaTimerActivity.LOG_TAG, "Timer/ onCreate has been called");
        setContentView(R.layout.timer_layout);
        
        // create buttons and other views
        resetExercise = (Button) findViewById(R.id.reset_exercise);
        resetFromStart = (Button) findViewById(R.id.reset_set_button);
        pause = (Button) findViewById(R.id.pause_button);
        currentExerciseName = (TextView) findViewById(R.id.exercise_name);
        timeDisplay = (TextView) findViewById(R.id.time_display);


                
        if (savedInstanceState == null) {
            // get the list of exercises
            Intent thisIntent = getIntent();
            Bundle listBundle = thisIntent.getExtras();
            try {
                exerciseList = (ArrayList<Exercise>) listBundle
                        .getSerializable("thelist");
            } catch (Exception e) {
                Log.e(YogaTimerActivity.LOG_TAG,
                        "Some thing went wrong when trying to get list");
            }
            completedExerciseStack = new Stack<Exercise>();
        }else{
            
            try {
                completedExerciseStack = (Stack<Exercise>) savedInstanceState.getSerializable(EXERCISE_STACK);
                exerciseList = (ArrayList<Exercise>) savedInstanceState.getSerializable(EXERCISE_LIST);
            } catch (Exception e) {
                
                Log.e(YogaTimerActivity.LOG_TAG, "problem getting old list or stack", e);

            }
            
            
        }
        
        Log.e(YogaTimerActivity.LOG_TAG,
                "here2");
        // display the list of exercises
        adapter = new ExerciseAdapter(this, exerciseList);
        setListAdapter(adapter);
         
        
        theListView = getListView();
        // when an exercise in the list is clicked, start from that exercise
        theListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position,
                    long arg3) {
                theCountDown.cancel();
                int p = position;
                completedExerciseStack.push(currentExercise);
                while (p != 0) {
                    completedExerciseStack.push(exerciseList.remove(0));
                    adapter.notifyDataSetChanged();
                    p--;
                }
                currentExercise = exerciseList.remove(0);
                adapter.notifyDataSetChanged();
                restartExercise();
            }

        });
        
        // when this button is clicked, restart from the beginning of current exercise
        resetExercise.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                restartExercise();
            }

        });
        
        // when this button is clicked, restart from the beginning of all the exercises
        resetFromStart.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                restartFromTop();
            }

        });
        
        // when this button is clicked call pause to pause
        pause.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(currentExercise.isPaused()){
                    unPauseTimer();
                }else{
                    pauseTimer();
                }
            }

        });
        
        //grab the first exercises
        currentExercise = exerciseList.remove(0);
        adapter.notifyDataSetChanged();
        
        // start with the first exercise
        //startCount(currentExercise);
    }

    public void onStart(){
        super.onStart();
        Log.d(YogaTimerActivity.LOG_TAG, "Timer/ onStart has been called");
    }
    
    public void onResume(){
        super.onResume();
        Log.d(YogaTimerActivity.LOG_TAG, "Timer/ onResume has been called");
        unPauseTimer();
    }
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        
        Log.d(YogaTimerActivity.LOG_TAG, "Timer/ onSaveInstanceState has been called");

        savedInstanceState.putSerializable(EXERCISE_STACK, completedExerciseStack);
        savedInstanceState.putSerializable(EXERCISE_LIST, exerciseList);
        
        super.onSaveInstanceState(savedInstanceState);
      }
    
    public void onPause(){
        Log.d(YogaTimerActivity.LOG_TAG, "Timer/ onPaused has been called");
        super.onPause();
        pauseTimer();
    }
    
    
    public void onStop(){
            super.onStop();
            Log.d(YogaTimerActivity.LOG_TAG, "Timer/ onStop has been called");
    }
    
    public void onDestroy(){
        super.onDestroy();
        Log.d(YogaTimerActivity.LOG_TAG, "Timer/ onDestroy has been called");
    }
    
    
    
    public void startCount(final Exercise ex) {
        
        // set the display to the name of the current exercise
        currentExerciseName.setText(ex.getName());
        
        long timeForCountDown;
        if (currentExercise.isPaused()) {
            timeForCountDown = ex.getPauseTime();
        } else
            timeForCountDown = ex.getTime();

        currentExercise.setStateUnPaused();
        theCountDown = new MyCounter(timeForCountDown, 1000, ex);
        theCountDown.start();

    }

    public void restartExercise() {
        currentExercise.setStateUnPaused();
        theCountDown.cancel();
        startCount(currentExercise);
    }

    public void pauseTimer() {
            currentExercise.setStatePaused(timeOnClock);
            theCountDown.cancel();
    }
    
    public void unPauseTimer() {
            startCount(currentExercise);
    }

    public void restartFromTop() {
        theCountDown.cancel();

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
            startCount(currentExercise);
        }
    }

    public class MyCounter extends CountDownTimer {
        Exercise ex;

        public MyCounter(long millisInFuture, long countDownInterval,
                Exercise ex) {
            super(millisInFuture, countDownInterval);
            this.ex = ex;
        }

        @Override
        public void onFinish() {
            if (!(exerciseList.isEmpty())) {
                completedExerciseStack.push(currentExercise);
                currentExercise = exerciseList.get(0);
                timeOnClock = currentExercise.getTime();
            }

            timeDisplay.setText("00:00");
            resetExercise.setClickable(false);
            resetFromStart.setClickable(false);
            MediaPlayer player = MediaPlayer.create(Timer.this, R.raw.jap_bell);
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer arg0) {
                    arg0.release();
                    if (!(exerciseList.isEmpty())) {
                        exerciseList.remove(0);
                        adapter.notifyDataSetChanged();
                        resetExercise.setClickable(true);
                        resetFromStart.setClickable(true);
                        if (!currentExercise.isPaused()) {

                            startCount(currentExercise);
                        } else
                            timeDisplay.setText(Exercise
                                    .timeToString(currentExercise.getTime()));
                        currentExerciseName.setText(currentExercise.getName());
                    } else
                        currentExerciseName.setText("All Finished");
                }
            });

        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeDisplay.setText(((Exercise.timeToString(millisUntilFinished))));
            timeOnClock = millisUntilFinished;

        }

    }
}
