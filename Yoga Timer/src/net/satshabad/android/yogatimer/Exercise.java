// Testing something
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
     * Creates an exercise
     * 
     * @param mName the name of the exercise
     * @param mTime the run time of the exercise in mili
     */
    public Exercise(String mName, long mTime) {
        name = mName;
        time = mTime;
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
