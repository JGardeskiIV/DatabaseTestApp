package edu.miracosta.cs134.databasetestapp;

import edu.miracosta.cs134.databasetestapp.WorkoutDialogFragment.OnWorkoutEnteredListener;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class WorkoutsGridFragment extends Fragment implements OnWorkoutEnteredListener {
	public static final String TAG = WorkoutsGridFragment.class.getSimpleName(); //used for Log.w()

	GridView mGrid;
	Button mAddButton;
	WorkoutsDbAdapter mWorkoutsDbAdapter;
	Cursor mWorkoutCursor;
	WorkoutAdapter mAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWorkoutsDbAdapter = new WorkoutsDbAdapter(getActivity());
		mWorkoutsDbAdapter.open();
		mWorkoutCursor = mWorkoutsDbAdapter.fetchWorkouts();
		mAdapter = new WorkoutAdapter(getActivity(), mWorkoutCursor);
		mGrid.setAdapter(mAdapter);
		int count = mWorkoutCursor.getCount();
		Log.w(TAG, "onActivityCreated() was called, Db has " + count + " rows");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.grid_fragment, container, false);
		mGrid = (GridView) v.findViewById(R.id.gridView1);
		mAddButton = (Button) v.findViewById(R.id.button1);
		
		mGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				mWorkoutCursor.moveToPosition(position);
				Workout clickedWorkout = WorkoutsDbAdapter.getWorkoutFromCursor(mWorkoutCursor);
				WorkoutDialogFragment workoutDialog = new WorkoutDialogFragment(clickedWorkout);
				workoutDialog.show(getFragmentManager(), "workoutDialog");
			}
		});
		
		mAddButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				WorkoutDialogFragment workoutDialog = new WorkoutDialogFragment();
				workoutDialog.show(getFragmentManager(), "workoutDialog");
			}
		});
		return v;
	}

	@Override
	public void OnWorkoutEntered(Workout workoutEntered, boolean isNewWorkout) {
		//change database
		if(isNewWorkout) {
			Log.w(TAG, "New workout added!");
			mWorkoutsDbAdapter.createWorkout(workoutEntered);
		} else {
			Log.w(TAG, "Old workout updated!");
			mWorkoutsDbAdapter.updateWorkout(workoutEntered);
		}
		//update cursor
		mWorkoutCursor = mWorkoutsDbAdapter.fetchWorkouts();
		//update view
		mAdapter = new WorkoutAdapter(getActivity(), mWorkoutCursor);
		mAdapter.notifyDataSetChanged();
		mGrid.setAdapter(mAdapter);
	}
	
}
