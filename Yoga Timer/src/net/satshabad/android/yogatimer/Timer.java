package net.satshabad.android.yogatimer;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class Timer extends ListActivity
{
   
   public ArrayList<Exercise> exerciseList;
   public long timeOnClock;   
   public boolean paused = false;
   public Exercise currentExercise;
   public MyCounter theCountDown;
   public ArrayList<Exercise> masterExerciseList;
   
   private TextView timeDisplay;
   private TextView currentExerciseName;
   
   private ExerciseAdapter adapter;
   
   private Button resetFromStart;
   private Button pause;
   private Button resetExercise;
   
  
   
   
   @SuppressWarnings("unchecked")
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.timer_layout);
      
      Intent thisIntent = getIntent();
      Bundle listBundle =  thisIntent.getExtras();
      exerciseList = (ArrayList<Exercise>) listBundle.getSerializable("thelist");
      masterExerciseList = (ArrayList<Exercise>) exerciseList.clone();
      adapter = new ExerciseAdapter(this, exerciseList);
      setListAdapter(adapter);

      resetExercise = (Button)findViewById(R.id.reset_exercise);
      resetFromStart = (Button)findViewById(R.id.reset_set_button);
      pause = (Button)findViewById(R.id.pause_button);

      currentExerciseName = (TextView)findViewById(R.id.exercise_name);
      timeDisplay = (TextView)findViewById(R.id.time_display);
      
      resetExercise.setOnClickListener(new Button.OnClickListener(){

         @Override
         public void onClick(View arg0)
         {
           paused = false;
           theCountDown.cancel();
           startCount(currentExercise);
         }
         
      });
      
      resetFromStart.setOnClickListener(new Button.OnClickListener(){

         @Override
         public void onClick(View arg0)
         {
            restartFromTop();
         }
         
      });
      
      pause.setOnClickListener(new Button.OnClickListener(){

        @Override
        public void onClick(View arg0)
        {
           pause();
        }
        
     });
            
      currentExercise = exerciseList.remove(0);
      adapter.notifyDataSetChanged();
      startCount(currentExercise);
   }
   
   public void startCount(final Exercise ex){
      currentExerciseName.setText(ex.getName());

      long timeForCountDown;
      if (paused){
         timeForCountDown = ex.getPauseTime();
      }
      else 
         timeForCountDown = ex.getTime();
         
      paused = false;
      theCountDown = new MyCounter(timeForCountDown, 1000, ex);
      theCountDown.start();

   }
   
   public void pause(){
      if (paused){
         startCount(currentExercise);
      }
      else {
         paused = true;
         currentExercise.setPauseTime(timeOnClock);
         theCountDown.cancel(); 
      }
   }
   
   public void restartFromTop(){
      theCountDown.cancel();
      exerciseList = masterExerciseList;
      masterExerciseList = (ArrayList<Exercise>) masterExerciseList.clone();
      adapter = new ExerciseAdapter(this, exerciseList);
      setListAdapter(adapter);
      currentExercise = exerciseList.remove(0);
      adapter.notifyDataSetChanged();
      startCount(currentExercise);
   }
   
   
   public class MyCounter extends CountDownTimer{
      Exercise ex;
      
      public MyCounter(long millisInFuture, long countDownInterval, Exercise ex)
      {
         super(millisInFuture, countDownInterval);
         this.ex = ex;
      }

      @Override
      public void onFinish()
      {
         if (!(exerciseList.isEmpty())){
            currentExercise = exerciseList.get(0);
            timeOnClock = currentExercise.getTime();
         }
  
         timeDisplay.setText("00:00");
         MediaPlayer player = MediaPlayer.create(Timer.this, R.raw.jap_bell);
         player.start();
         player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
         
            public void onCompletion(MediaPlayer arg0){
               arg0.release();
               if (!(exerciseList.isEmpty())){
                  exerciseList.remove(0);
                  adapter.notifyDataSetChanged();
                  if (!paused)
                  {  
                     startCount(currentExercise);
                  }
                  else
                     timeDisplay.setText(currentExercise.timeToString(currentExercise.getTime()));
                     currentExerciseName.setText(currentExercise.getName());
               }
               else
                  currentExerciseName.setText("All Finished");
            }
         });
         
      }

      @Override
      public void onTick(long millisUntilFinished)
      {
         timeDisplay.setText(((ex.timeToString(millisUntilFinished))));
         timeOnClock = millisUntilFinished;
         
      }
      
   }
}
