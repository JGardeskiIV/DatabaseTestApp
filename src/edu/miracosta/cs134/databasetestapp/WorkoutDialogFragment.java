package edu.miracosta.cs134.databasetestapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class WorkoutDialogFragment extends DialogFragment  {
	public interface OnWorkoutEnteredListener {
		public void OnWorkoutEntered(Workout workoutEntered, boolean isNewWorkout);
	}
	OnWorkoutEnteredListener mListener;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnWorkoutEnteredListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnWorkoutEnteredListener");
        }
    }
	Button mOK;
	Button mCancel;
	EditText mNameField;
	EditText mRepsField;
	EditText mSetsField;
	Workout clickedWorkout;
	boolean isNewWorkout;
	
	//adding a new workout!
	public WorkoutDialogFragment()
	{
		clickedWorkout = null;
		isNewWorkout = true;
	}
	//modifying an old workout!
	public WorkoutDialogFragment(Workout cWorkout) {
		clickedWorkout = cWorkout;
		isNewWorkout = false;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		View v = inflater.inflate(R.layout.fragment_dialog_workout, container, false);
    	getDialog().setTitle("Workout Details");
//    	getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    	mNameField = (EditText)v.findViewById(R.id.editText1);
    	mRepsField = (EditText)v.findViewById(R.id.editText2);
    	mSetsField = (EditText)v.findViewById(R.id.editText3);

    	//set values if exist for fields!
    	if(!isNewWorkout) {
    		mNameField.setText(clickedWorkout.getName());
    		mRepsField.setText(clickedWorkout.getReps() + "");
    		mSetsField.setText(clickedWorkout.getSets() + "");
    	}
    	
		mOK = (Button)v.findViewById(R.id.buttonOK); 
		mOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String name = mNameField.getText().toString();
				int reps = Integer.parseInt(mRepsField.getText().toString());
				int sets = Integer.parseInt(mSetsField.getText().toString());
				boolean isRepsValid = reps >= 0 && reps <= 1000;
				boolean isSetsValid = sets >= 0 && sets <= 1000;

				if(!isRepsValid) {
					mRepsField.setText("");
					mRepsField.setHint("ERROR: 0-100 reps. Try again.");
				}
				if(!isSetsValid) {
					mSetsField.setText("");
					mSetsField.setHint("ERROR: 0-100 sets. Try again.");
				}
				if(isRepsValid && isSetsValid) {
					long id = isNewWorkout ? 0L : clickedWorkout.getId();
					Workout w = new Workout(id, name, reps, sets);
					mListener.OnWorkoutEntered(w, isNewWorkout);
					WorkoutDialogFragment.this.dismiss();
				}
			}
		});
		mCancel = (Button)v.findViewById(R.id.buttonCancel);
		mCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				WorkoutDialogFragment.this.dismiss();
			}
		});
		return v;
	}
}
