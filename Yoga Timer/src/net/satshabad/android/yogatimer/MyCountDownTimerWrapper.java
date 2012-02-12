package net.satshabad.android.yogatimer;

import java.io.Serializable;

public class MyCountDownTimerWrapper implements Serializable {

	private transient TimerActivity timerActivity;
	private MyCountDownTimer countDownTimer;
	private boolean isRunning;
	private static final long ONE_SECOND = 1000;
	private boolean isPaused;
	private long pauseTime;
	
	
	public MyCountDownTimerWrapper(TimerActivity timerActivity) {
		this.timerActivity = timerActivity;
		countDownTimer = null;
		isRunning= false;
		isPaused = false;
		
		
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void stopTimer() {
		if(!isRunning){
			return;
		}
		countDownTimer.cancel();
		isRunning = false;
		isPaused = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void startTimer(long time) {
		
		if(isRunning){
			return;
		}
		countDownTimer = new MyCountDownTimer(time, ONE_SECOND, this);
		countDownTimer.start();
		isRunning = true;
		isPaused = false;
		pauseTime = time;
	}

	public void pauseTimer() {
		if(!isRunning || isPaused){
			return;
		}
		countDownTimer.cancel();
		countDownTimer = null;
		isPaused = true;
	}

	public void unPauseTimer() {
		if(!isRunning || !isPaused){
			return;
		}
		countDownTimer = new MyCountDownTimer(pauseTime, ONE_SECOND, this);
		countDownTimer.start();
		isPaused = false;
	}

	public void timerFinish() {
		timerActivity.timerFinish();
		
	}

	public void timerTick(long millisUntilFinished) {
		timerActivity.timerTick(millisUntilFinished);
		pauseTime = millisUntilFinished;
		
	}

	public void setActivity(TimerActivity t) {
		timerActivity =t;
		
	}

}
