package net.satshabad.android.yogatimer;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * This class is currently not in use, but could potentially be used to make the
 * code more modular by abstracting out the counter from the main logic.
 * 
 * @author satshabad
 * 
 */
public class MyCountDownTimer extends CountDownTimer {
	private MyCountDownTimerWrapper ActivityWaitingForResponse;
	private long timeLeft;
	
	
	public MyCountDownTimer(long millisInFuture, long countDownInterval,
			MyCountDownTimerWrapper mActivityWaitingForResponse) {
		super(millisInFuture, countDownInterval);
		ActivityWaitingForResponse = mActivityWaitingForResponse;
		timeLeft = millisInFuture;
	}

	@Override
	public void onFinish() {
		ActivityWaitingForResponse.timerFinish();
		

	}

	@Override
	public void onTick(long millisUntilFinished) {
		timeLeft = millisUntilFinished;
		ActivityWaitingForResponse.timerTick(millisUntilFinished);
	}

	public long getTimeLeft() {
		return timeLeft;
	}

}
