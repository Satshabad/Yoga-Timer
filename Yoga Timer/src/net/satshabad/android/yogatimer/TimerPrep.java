package net.satshabad.android.yogatimer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

public class TimerPrep extends ListActivity {

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
     * The total number of times the user has tapped on any number button
     */
    private int numberOfClicks = 0;

    /**
     * A view containing the name of the exercise, which can be changed by the
     * user
     */
    private EditText exerciseName;

    /**
     * A container for the timer setting mechanism
     */
    private SlidingDrawer drawer;

    /**
     * A list holding the exercises entered by the user
     */
    private ArrayList<Exercise> exerciseList;

    /**
     *An adapter to hold the elements in {@link #exerciseList} and then display them in a list
     */
    private ExerciseAdapter adapter;
    
    
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

        exerciseList = new ArrayList<Exercise>();

        adapter = new ExerciseAdapter(this, exerciseList);
        setListAdapter(adapter);
        firstDigit = (TextView) findViewById(R.id.time_display_first_digit);
        secondDigit = (TextView) findViewById(R.id.time_display_second_digit);
        thirdDigit = (TextView) findViewById(R.id.time_display_third_digit);
        fourthDigit = (TextView) findViewById(R.id.time_display_fourth_digit);

        exerciseName = (EditText) findViewById(R.id.exercise_name);
        exerciseName.selectAll();
        drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
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
            int fourth = Integer.parseInt(fourthDigit.getText().toString());
            int third = Integer.parseInt(thirdDigit.getText().toString());
            int second = Integer.parseInt(secondDigit.getText().toString());
            int first = Integer.parseInt(firstDigit.getText().toString());

            long seconds = (long) (second * 10) + first;
            long minutes = (long) (fourth * 10) + third;
            long time = (seconds * 1000) + (minutes * 60000);

            exerciseList.add(exerciseIndex, new Exercise(exerciseName.getText()
                    .toString(), time));

            exerciseIndex++;
            ;
            exerciseName.setText("Exercise " + numbers[exerciseIndex]);
            adapter.notifyDataSetChanged();
            drawer.close();

            updateDisplay(-1);
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

    public void startTimer(View v) {
        Intent newIntent = new Intent(TimerPrep.this, Timer.class);
        newIntent.putExtra("thelist", exerciseList);
        startActivity(newIntent);

    }
}
