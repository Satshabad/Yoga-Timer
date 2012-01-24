package net.satshabad.android.yogatimer;

/**
 * This interface defines the object used by the ontimesetlistener method.
 * 
 * 
 * @author satshabad
 *
 */
public interface OnTimeSetListener {
    public abstract void onTimeSet(String exerciseName, long time);
}
