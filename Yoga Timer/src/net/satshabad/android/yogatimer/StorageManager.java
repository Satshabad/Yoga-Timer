package net.satshabad.android.yogatimer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class StorageManager {

	private final String LIST = "_LIST";
	private final String STACK = "_STACK";
	private final String COUNT = "_COUNT";
	private final String RUNNING = "RUNNING";

	public void putRunningList(ArrayList<Exercise> list, String key,
			Activity callingActivity) {
		putRunningObject(list, key, "running list", callingActivity);

	}

	public void putRunningStack(Stack<Exercise> stack, String key,
			Activity callingActivity) {
		putRunningObject(stack, key, "running stack", callingActivity);
	}

	public void putRunningCountDownTimer(
			MyCountDownTimerWrapper countDownTimer, String key,
			Activity callingActivity) {

		putRunningObject(countDownTimer, key, "running count down timer",
				callingActivity);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Exercise> getRunningList(String key,
			Activity callingActivity) {
		return (ArrayList<Exercise>) getRunningObject(key, "running list", callingActivity);
	}

	@SuppressWarnings("unchecked")
	public Stack<Exercise> getRunningStack(String key, Activity callingActivity) {
		return (Stack<Exercise>) getRunningObject(key, "running stack", callingActivity);
	}

	public MyCountDownTimerWrapper getRunningCountDownTimer(String key,
			Activity callingActivity) {
		return (MyCountDownTimerWrapper) getRunningObject(key, "running count down timer", callingActivity);
	}

	public void putSet(ArrayList<Exercise> list, String key,
			Activity callingActivity) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {

			fout = callingActivity.openFileOutput(key, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(list);

		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not put set to file");
			e.printStackTrace();
		} finally {
			try {
				if (oos != null && fout != null) {
					oos.close();
					fout.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close running set files in attempt to get put set");
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Exercise> getSet(String key, Activity callingActivity) {
		FileInputStream file = null;
		ObjectInputStream input = null;
		try {
			file = callingActivity.openFileInput(key);
			input = new ObjectInputStream(file);
			return (ArrayList<Exercise>) input.readObject();
		} catch (StreamCorruptedException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get set from file");
			e.printStackTrace();
		} catch (OptionalDataException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get set from file");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get set from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get set from file");
			e.printStackTrace();
		} finally {

			try {
				if (file != null && input != null) {
					file.close();
					input.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close files while getting set from file");
				e.printStackTrace();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Object getRunningObject(String key, String description,
			Activity callingActivity) {
		FileInputStream file = null;
		ObjectInputStream input = null;
		try {
			file = callingActivity.openFileInput(RUNNING + key);
			input = new ObjectInputStream(file);
			if (key == COUNT) {
				return (MyCountDownTimerWrapper) input.readObject();
			} else if (key == LIST) {
				return (ArrayList<Exercise>) input.readObject();
			} else if (key == STACK) {
				return (Stack<Exercise>) input.readObject();
			}
		} catch (StreamCorruptedException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get running "
					+ description + " from file");
			e.printStackTrace();
		} catch (OptionalDataException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get running "
					+ description + " from file");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get running "
					+ description + " from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(MainMenuActivity.LOG_TAG, "Could not get running"
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
						"Could not close files while getting running "
								+ description + " from file");
				e.printStackTrace();
			}

		}
		return null;
	}

	private void putRunningObject(Object object, String key,
			String description, Activity callingActivity) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {

			fout = callingActivity.openFileOutput(RUNNING + key,
					Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(object);

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
