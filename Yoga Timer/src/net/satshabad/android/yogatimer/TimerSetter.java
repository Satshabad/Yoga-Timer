package net.satshabad.android.yogatimer;

import android.content.Context;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



public class TimerSetter extends LinearLayout {

    OnTimeSetListener timeSetListener = null;

    
    /**
     * A Text View of the first digit in the time display contained in the timer
     * setting drawer
     */
    private TextView firstDigit;

    /**
     * A Text View of the second digit in the time display contained in the
     * timer setting drawer
     */
    private TextView secondDigit;

    /**
     * A Text View of the third digit in the time display contained in the timer
     * setting drawer
     */
    private TextView thirdDigit;

    /**
     * A Text View of the fourth digit in the time display contained in the
     * timer setting drawer
     */
    private TextView fourthDigit;
    
    /**
     * A view containing the name of the exercise, which can be changed by the
     * user
     */
    private EditText exerciseName;
    
    /**
     * The total number of times the user has tapped on any number button
     */
    private int numberOfClicks = 0;
    
    public TimerSetter(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.timersetter_layout, this);
        firstDigit = (TextView) findViewById(R.id.time_display_first_digit);
        secondDigit = (TextView) findViewById(R.id.time_display_second_digit);
        thirdDigit = (TextView) findViewById(R.id.time_display_third_digit);
        fourthDigit = (TextView) findViewById(R.id.time_display_fourth_digit);
        TimerSetter.OnTimerButtonClickListener clickListener = new OnTimerButtonClickListener();
        Button one = (Button) findViewById(R.id.button_one);
        one.setOnClickListener(clickListener);
        
        findViewById(R.id.button_two).setOnClickListener(clickListener);
        findViewById(R.id.button_three).setOnClickListener(clickListener);
        findViewById(R.id.button_four).setOnClickListener(clickListener);
        findViewById(R.id.button_five).setOnClickListener(clickListener);
        findViewById(R.id.button_six).setOnClickListener(clickListener);
        findViewById(R.id.button_seven).setOnClickListener(clickListener);
        findViewById(R.id.button_eight).setOnClickListener(clickListener);
        findViewById(R.id.button_nine).setOnClickListener(clickListener);
        findViewById(R.id.button_zero).setOnClickListener(clickListener);
        findViewById(R.id.button_clear).setOnClickListener(clickListener);
        findViewById(R.id.button_ok).setOnClickListener(clickListener);
        
        
    }

    public void timerClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (v.getId()) {
        case R.id.button_one: {
            numberOfClicks++;
            updateDisplay(1);

        }
            break;
        case R.id.button_two: {
            numberOfClicks++;
            updateDisplay(2);

        }
            break;
        case R.id.button_three: {
            numberOfClicks++;
            updateDisplay(3);

        }
            break;
        case R.id.button_four: {
            numberOfClicks++;
            updateDisplay(4);

        }
            break;
        case R.id.button_five: {
            numberOfClicks++;
            updateDisplay(5);

        }
            break;
        case R.id.button_six: {
            numberOfClicks++;
            updateDisplay(6);

        }
            break;
        case R.id.button_seven: {
            numberOfClicks++;
            updateDisplay(7);

        }
            break;
        case R.id.button_eight: {
            numberOfClicks++;
            updateDisplay(8);

        }
            break;
        case R.id.button_nine: {
            numberOfClicks++;
            updateDisplay(9);

        }
            break;
        case R.id.button_zero: {
            numberOfClicks++;
            updateDisplay(0);

        }
            break;
        case R.id.button_ok: {
            long time = getTime();
            String name = getName();
            OnTimeSet(name, time);
        }
            break;
        case R.id.button_clear: {
            updateDisplay(-1);
        }
            break;
        }

    }
    
    private void updateDisplay(int n) {

        if (n == -1) {
            firstDigit.setText("0");
            secondDigit.setText("0");
            thirdDigit.setText("0");
            fourthDigit.setText("0");
            numberOfClicks = 0;
        }

        if (numberOfClicks == 1) {
            Integer digit = new Integer(n);
            firstDigit.setText(digit.toString());
        }

        if (numberOfClicks == 2) {
            Integer digit = new Integer(n);
            secondDigit.setText(firstDigit.getText());
            firstDigit.setText(digit.toString());
        }

        if (numberOfClicks == 3) {
            Integer digit = new Integer(n);
            thirdDigit.setText(secondDigit.getText());
            secondDigit.setText(firstDigit.getText());
            firstDigit.setText(digit.toString());
        }

        if (numberOfClicks == 4) {
            Integer digit = new Integer(n);
            fourthDigit.setText(thirdDigit.getText());
            thirdDigit.setText(secondDigit.getText());
            secondDigit.setText(firstDigit.getText());
            firstDigit.setText(digit.toString());
        }

    }

    private long getTime() {
        int fourth = Integer.parseInt(fourthDigit.getText().toString());
        int third = Integer.parseInt(thirdDigit.getText().toString());
        int second = Integer.parseInt(secondDigit.getText().toString());
        int first = Integer.parseInt(firstDigit.getText().toString());
        long seconds = (long) (second * 10) + first;
        long minutes = (long) (fourth * 10) + third;
        long time = (seconds * 1000) + (minutes * 60000);
        updateDisplay(-1);
        return time;
    }

    private String getName() {
        exerciseName = (EditText) findViewById(R.id.exercise_name);
        return exerciseName.getText().toString();
    }
    
    private void OnTimeSet(String name, long time){
        // Check if the Listener was set, otherwise we'll get an Exception when we try to call it
        if(!(timeSetListener==null)) {
            // Only trigger the event, when we have valid params
            if(time != 0 && name != ""){
                timeSetListener.onTimeSet(name, time);
            }
        }
    }
    
    public void setOnTimeSetListener(OnTimeSetListener listener) {
        timeSetListener = listener;
    }

    public void setNextTimerName(String string) {
       exerciseName.setText(string);
       exerciseName.selectAll();
    }

    public void onFinishInflate(){
        super.onFinishInflate();

    }
    
    private class OnTimerButtonClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
           Log.d(YogaTimerActivity.LOG_TAG, "here");
           timerClick(v);
        }    
    }
}


