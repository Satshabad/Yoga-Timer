package net.satshabad.android.yogatimer;

import java.io.Serializable;


public class Exercise implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8050439710951413000L;

    /**
     * The name of the exercise
     */
    private String name;
    
    /**
     * The run time of the exercise
     */
    private long time;
    
    /**
     * If the exercise has been paused then this is the time left, if not then it is the equal to the run time
     */
    private long pauseTime;
    
    /**
     * If the exercises has been paused or not
     * 
     */
    private boolean paused;

    
    /**
     * Creates an exercise
     * 
     * @param mName the name of the exercise
     * @param mTime the run time of the exercise in mili
     */
    public Exercise(String mName, long mTime) {
        name = mName;
        time = mTime;
        paused = true;
        pauseTime = mTime;
    }

    /**
     * Gets the name of the exercise
     * 
     * @return the name of the exercise
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the total runtime of the exercise
     * 
     * @return the time in milliseconds
     */
    public long getTime() {
        return time;
    }

    /**
     * Check whether the exercise is paused or not
     * 
     * @return
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Pauses the current exercise. If already paused, does nothing
     */
    public void setStatePaused(long t) {
        paused = true;
        pauseTime = t;
    }

    /**
     * Unpauses the current exercise. If already unpaused, does nothing
     */
    public void setStateUnPaused() {
        pauseTime = time;
        paused = false;
    }

    /**
     * Gets the time left
     * 
     * @return
     */
    public long getPauseTime() {

        return pauseTime;
    }

    /**
     * Gets the time in a ##:## format
     * 
     * @param t 
     * @return The time in the correct format
     */
    public  static String timeToString(long t) {
        long totalSeconds = t / 1000;
        String seconds = Integer.toString((int) (totalSeconds % 60));
        String minutes = Integer.toString((int) ((totalSeconds % 3600) / 60));
        String hours = Integer.toString((int) (totalSeconds / 3600));
        for (int i = 0; i < 2; i++) {
            if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }
            if (minutes.length() < 2) {
                minutes = "0" + minutes;
            }
            if (hours.length() < 2) {
                hours = "0" + hours;
            }
        }
        if (hours.equals("00")) {

            return "" + minutes + ":" + seconds;
        }

        return "" + hours + ":" + minutes + ":" + seconds;

    }
}
