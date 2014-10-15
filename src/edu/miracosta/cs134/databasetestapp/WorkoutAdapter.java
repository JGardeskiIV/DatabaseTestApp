package edu.miracosta.cs134.databasetestapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WorkoutAdapter extends BaseAdapter {
	public static final String TAG = WorkoutAdapter.class.getSimpleName();
	Context mContext;
	Cursor mWorkoutCursor;
	LayoutInflater mInflater;
	
	public WorkoutAdapter(Context c, Cursor wc) {
		mContext = c;
		mWorkoutCursor = wc;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return mWorkoutCursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		mWorkoutCursor.moveToPosition(position);
		return WorkoutsDbAdapter.getWorkoutFromCursor(mWorkoutCursor);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		Workout currentWorkout;
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView)convertView.findViewById(R.id.textView1);
			viewHolder.reps = (TextView)convertView.findViewById(R.id.textView2);
			viewHolder.sets = (TextView)convertView.findViewById(R.id.textView4);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		currentWorkout = (Workout)getItem(position);
		Log.w(TAG, "view for workout = " + currentWorkout);
		viewHolder.name.setText(currentWorkout.getName());
		viewHolder.reps.setText(currentWorkout.getReps() + "");
		viewHolder.sets.setText(currentWorkout.getSets() + "");

		return convertView;
	}
	
	private static class ViewHolder {
		public TextView name;
		public TextView reps;
		public TextView sets;
	}

}
