package net.satshabad.android.timersetter;

import net.satshabad.android.yogatimer.R;
import net.satshabad.android.yogatimer.R.id;
import net.satshabad.android.yogatimer.R.layout;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class displays a box with a key pad and has a call back method to pass a
 * time in minutes and seconds when the users endters "OK"
 * 
 * @author satshabad
 * 
 */
public class TimerSetter extends LinearLayout {

	/**
	 * The listener object that contains the method to call when the OK button
	 * is pressed
	 */
	OnTimeSetListener timeSetListener = null;

	/**
	 * A Text View of the first digit in the time display
	 */
	private TextView firstDigit;

	/**
	 * A Text View of the second digit in the time display
	 */
	private TextView secondDigit;

	/**
	 * A Text View of the third digit in the time display
	 */
	private TextView thirdDigit;

	/**
	 * A Text View of the fourth digit in the time display
	 */
	private TextView fourthDigit;

	/**
	 * A view containing the name of the timer, which can be changed by the user
	 */
	private EditText name;

	/**
	 * The total number of times the user has tapped on any number button. Used
	 * to correctly update the display. Probably could be refractored to avoid
	 * using global variables
	 */
	private int numberOfClicks = 0;

	public TimerSetter(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.timersetter_layout, this);

		// grab the display views
		firstDigit = (TextView) findViewById(R.id.time_display_first_digit);
		secondDigit = (TextView) findViewById(R.id.time_display_second_digit);
		thirdDigit = (TextView) findViewById(R.id.time_display_third_digit);
		fourthDigit = (TextView) findViewById(R.id.time_display_fourth_digit);
		name = (EditText) findViewById(R.id.timer_name);

		// use the innerclass a click listener for all these buttons, which lead
		// all to the same method, timerClick
		TimerSetter.OnTimerButtonClickListener clickListener = new OnTimerButtonClickListener();

		// set click listeners for all the buttons
		findViewById(R.id.button_one).setOnClickListener(clickListener);
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

	/**
	 * Called whenever a button (0 - 9, clear or OK) is pressed. Takes the
	 * appropriate action. Either updating the display or clearing it or in the
	 * case of "OK" calling the call back method onTimeSet so user knows that
	 * the timer has been set.
	 * 
	 * @param v
	 *            the view which activated the call
	 */
	private void timerClick(View v) {

		// vibrate when a button is pressed
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
			if (time == 0) {
				return;
			}
			String name = getName();
			OnTimeSet(name, time);
		}
			break;
		case R.id.button_clear: {
			updateDisplay(-1);
		}
			break;
		default:

			// this method should never be passed a view that is not one of the
			// above buttons
			assert (false);

		}

	}

	/**
	 * Updates the timer display, adding the new passed digit to the end of the
	 * display
	 * 
	 * @param n
	 *            the digit to be added, if -1 then the display will be cleared
	 */
	private void updateDisplay(int n) {

		// clear the display back to zeros
		if (n == -1) {
			firstDigit.setText("0");
			secondDigit.setText("0");
			thirdDigit.setText("0");
			fourthDigit.setText("0");
			numberOfClicks = 0;
		}

		// keeping track of the number of clicks lets the display update the
		// right digit
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

	/**
	 * Gets the length of the timer from the display
	 * 
	 * @return the timer length in milliseconds
	 */
	private long getTime() {
		int fourth = Integer.parseInt(fourthDigit.getText().toString());
		int third = Integer.parseInt(thirdDigit.getText().toString());
		int second = Integer.parseInt(secondDigit.getText().toString());
		int first = Integer.parseInt(firstDigit.getText().toString());
		long seconds = (long) (second * 10) + first;
		long minutes = (long) (fourth * 10) + third;
		long time = (seconds * 1000) + (minutes * 60000);

		// clear the display
		updateDisplay(-1);
		return time;
	}

	/**
	 * Retrieves the name of the timer from the edittext box
	 * 
	 * @return the name of timer
	 */
	private String getName() {
		return name.getText().toString();
	}

	/**
	 * When this method is called it tried to call
	 * {@link #OnTimeSet(String, long)} with the passes args
	 * 
	 * @param name
	 *            the name of the timer
	 * @param time
	 *            the time on the timer
	 */
	private void OnTimeSet(String name, long time) {
		// Check if the Listener was set, otherwise we'll get an Exception when
		// we try to call it
		if (!(timeSetListener == null)) {
			// Only trigger the event, when we have valid params
			if (time != 0 && name != "") {
				timeSetListener.onTimeSet(name, time);
			}
		}
	}

	/**
	 * This method allows the user to pass a
	 * {@link #setOnTimeSetListener(OnTimeSetListener)} object that can be used
	 * to call the user's implemented method later
	 * 
	 * @param listener
	 *            the listener object which implements the OnTimeSetListener
	 *            interface
	 */
	public void setOnTimeSetListener(OnTimeSetListener listener) {
		timeSetListener = listener;
	}

	/**
	 * A kludge method that allows the exercise name to be set in advance by the
	 * caller rather than the user.
	 * 
	 * @param string
	 *            the name of the field
	 */
	public void setNextTimerName(String string) {
		name.setText(string);
		name.selectAll();
	}

	/**
	 * This custom click listener class allows for a more succinct way to let
	 * the buttons call the timerClick method rather than setting inline
	 * interface implementation for all of them or having them call a method in
	 * xml
	 * 
	 * @author satshabad
	 * 
	 */
	private class OnTimerButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			timerClick(v);
		}
	}
}
