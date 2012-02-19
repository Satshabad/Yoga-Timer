package net.satshabad.android.yogatimer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class helps display the {@link #exercises} objects correctly in a list
 * 
 * @author satshabad
 *
 */
public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private ArrayList<Exercise> exercises;
    private Context context;

    public ExerciseAdapter(Context context, List<Exercise> objects) {
        super(context, R.layout.exercise_row, R.id.name_in_row, objects);
        exercises = (ArrayList<Exercise>) objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        TextView exerciseName = (TextView) row.findViewById(R.id.name_in_row);
        // set the text view text to the exercise name
        exerciseName.setText(exercises.get(position).getName());
        TextView exerciseTime = (TextView) row.findViewById(R.id.time_in_row);
        // set the text view text to the exercise time, while formatting it
        exerciseTime.setText(Exercise.timeToString(exercises.get(position)
                .getTime()));

        return row;

    }
}
