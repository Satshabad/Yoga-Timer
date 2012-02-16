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

	private static final String LIST = "_LIST";
	private static final String STACK = "_STACK";
	private static final String COUNT = "_COUNT";

	public static void putRunningList(ArrayList<Exercise> list, String key,
			Activity callingActivity) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {

			fout = callingActivity.openFileOutput(key+LIST, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(list);

		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not put running list to file");
			e.printStackTrace();
		} finally {
			try {
				if (oos != null && fout != null) {
					oos.close();
					fout.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close running list files in attempt to get list");
				e.printStackTrace();
			}
		}
	}

	public static void putRunningStack(Stack<Exercise> stack, String key,
			Activity callingActivity) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {

			fout = callingActivity.openFileOutput(key+STACK, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(stack);

		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not put running stack to file");
			e.printStackTrace();
		} finally {
			try {
				if (oos != null && fout != null) {
					oos.close();
					fout.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close running stack files in attempt to get list");
				e.printStackTrace();
			}
		}
	}

	public static void putRunningCountDownTimer(
			MyCountDownTimerWrapper countDownTimer, String key,
			Activity callingActivity) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {

			fout = callingActivity.openFileOutput(key+COUNT, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(countDownTimer);

		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not put running count down timer to file");
			e.printStackTrace();
		} finally {
			try {
				if (oos != null && fout != null) {
					oos.close();
					fout.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close running count down timer files in attempt to put list");
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Exercise> getRunningList(String key, Activity callingActivity) {
		FileInputStream file = null;
		ObjectInputStream input= null;
		try {
		    file = callingActivity.openFileInput(key+LIST);
			input = new ObjectInputStream(file);
			return (ArrayList<Exercise>) input.readObject();
		} catch (StreamCorruptedException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running list from file");
			e.printStackTrace();
		} catch (OptionalDataException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running list from file");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running list from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running list from file");
			e.printStackTrace();
		}finally{
			
			try {
				if (file != null && input != null) {
					file.close();
					input.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close files while getting running list from file");
				e.printStackTrace();
			}
			
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Stack<Exercise> getRunningStack(String key, Activity callingActivity) {
		FileInputStream file = null;
		ObjectInputStream input= null;
		try {
		    file = callingActivity.openFileInput(key+STACK);
			input = new ObjectInputStream(file);
			return (Stack<Exercise>) input.readObject();
		} catch (StreamCorruptedException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running stack from file");
			e.printStackTrace();
		} catch (OptionalDataException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running stack from file");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running stack from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running stack from file");
			e.printStackTrace();
		}finally{
			
			try {
				if (file != null && input != null) {
					file.close();
					input.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close files while getting running stack from file");
				e.printStackTrace();
			}
			
		}
		return null;
	}

	public static MyCountDownTimerWrapper getRunningCountDownTimer(String key, Activity callingActivity) {
		FileInputStream file = null;
		ObjectInputStream input= null;
		try {
		    file = callingActivity.openFileInput(key+COUNT);
			input = new ObjectInputStream(file);
			return (MyCountDownTimerWrapper) input.readObject();
		} catch (StreamCorruptedException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running count down timer from file");
			e.printStackTrace();
		} catch (OptionalDataException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running count down timer from file");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running count down timer from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get running count down timer from file");
			e.printStackTrace();
		}finally{
			
			try {
				if (file != null && input != null) {
					file.close();
					input.close();
				}
			} catch (IOException e) {
				Log.e(MainMenuActivity.LOG_TAG,
						"Could not close files while getting running count down timer from file");
				e.printStackTrace();
			}
			
		}
		return null;
	}

	public static void putSet(ArrayList<Exercise> list, String key, Activity callingActivity) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {

			fout = callingActivity.openFileOutput(key, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(list);

		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not put set to file");
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
	public static ArrayList<Exercise> getSet(String key, Activity callingActivity) {
		FileInputStream file = null;
		ObjectInputStream input= null;
		try {
		    file = callingActivity.openFileInput(key);
			input = new ObjectInputStream(file);
			return (ArrayList<Exercise>) input.readObject();
		} catch (StreamCorruptedException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get set from file");
			e.printStackTrace();
		} catch (OptionalDataException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get set from file");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get set from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(MainMenuActivity.LOG_TAG,
					"Could not get set from file");
			e.printStackTrace();
		}finally{
			
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

}
