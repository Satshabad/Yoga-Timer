package net.satshabad.android.yogatimer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class StorageManager {

	private final String LIST = "_LIST";
	private final String STACK = "_STACK";
	private final String COUNT = "_COUNT";
	private final String KEYS = "_KEYS";
	

	public void putRunningList(ArrayList<Exercise> list,
			Activity callingActivity) {
		putObject(list, LIST, "running list", callingActivity);

	}

	public void putRunningStack(Stack<Exercise> stack, Activity callingActivity) {
		putObject(stack, STACK,"running stack", callingActivity);
	}

	public void putRunningCountDownTimer(
			MyCountDownTimerWrapper countDownTimer,
			Activity callingActivity) {

		putObject(countDownTimer,COUNT,"running count down timer",
				callingActivity);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Exercise> getRunningList(Activity callingActivity) {
		return (ArrayList<Exercise>) getObject(LIST, "running list", callingActivity);
	}

	@SuppressWarnings("unchecked")
	public Stack<Exercise> getRunningStack(Activity callingActivity) {
		return (Stack<Exercise>) getObject(STACK, "running stack", callingActivity);
	}

	public MyCountDownTimerWrapper getRunningCountDownTimer(Activity callingActivity) {
		return (MyCountDownTimerWrapper) getObject(COUNT, "running count down timer", callingActivity);
	}

	public void putSet(ArrayList<Exercise> list, String key,
			Activity callingActivity) {
		putObject(list, key, "set", callingActivity);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Exercise> getSet(String key, Activity callingActivity) {
		return (ArrayList<Exercise>) getObject(key, "set", callingActivity);
	}

	public void putKeysToFile(ArrayList<String> keysList, Activity callingActivity){
		putObject(keysList, KEYS, "set keys", callingActivity);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getSetKeysFromFile(Activity callingActivity) {
		return (ArrayList<String>) getObject(KEYS, "set keys", callingActivity);
		
	}
	
	public void addSetKey(String setName, Activity callingActivity) {
		ArrayList<String> keysList = getSetKeysFromFile(callingActivity);
		keysList.add(setName);
		putKeysToFile(keysList, callingActivity);
	}
	
	@SuppressWarnings("unchecked")
	private Object getObject(String key, String description,
			Activity callingActivity) {
		FileInputStream file = null;
		ObjectInputStream input = null;
		try {
			file = callingActivity.openFileInput(key);
			input = new ObjectInputStream(file);
			if (key == COUNT) {
				return (MyCountDownTimerWrapper) input.readObject();
			} else if (key == LIST) {
				return (Stack<Exercise>) input.readObject();
			} else if (key == STACK) {
				return (Stack<Exercise>) input.readObject();
			} else if(key == KEYS){
				return (ArrayList<String>) input.readObject();
			}else{
				return (ArrayList<Exercise>) input.readObject();
			}
		} catch (StreamCorruptedException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get "
					+ description + " from file");
			e.printStackTrace();
		} catch (OptionalDataException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get "
					+ description + " from file");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get "
					+ description + " from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get "
					+ description + " from file");
			e.printStackTrace();
		} finally {

			try {
				if (file != null && input != null) {
					file.close();
					input.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close files while getting "
								+ description + " from file");
				e.printStackTrace();
			}

		}
		return null;
	}

	private void putObject(Object object, String key,
			String description, Activity callingActivity) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {

			fout = callingActivity.openFileOutput(key,
					Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(object);
			Log.d(MainMenuActivity.LOG_TAG,
					"Wrote file to " + fout.toString());
		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not put" + description
					+ "to file");
			e.printStackTrace();
		} finally {
			try {
				if (oos != null && fout != null) {
					oos.close();
					fout.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close files in attempt to get" + description);
				e.printStackTrace();
			}
		}
	}

}
