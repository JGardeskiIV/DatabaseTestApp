package edu.miracosta.cs134.databasetestapp;

import edu.miracosta.cs134.databasetestapp.WorkoutDialogFragment.OnWorkoutEnteredListener;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements OnWorkoutEnteredListener {
	public static final String TAG = MainActivity.class.getSimpleName(); //used for Log.w()

	Tab mTab1;
	Tab mTab2;
	WorkoutsGridFragment workoutsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mTab1= actionBar.newTab().setText("Workouts").setTabListener(new NavTabListener());
		mTab2= actionBar.newTab().setText("Weight Tracker").setTabListener(new NavTabListener());

		actionBar.addTab(mTab1);
		actionBar.addTab(mTab2);
		
		showWorkouts();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showWeights(){
		// TODO on next sprint
	}
	
	public void showWorkouts(){
		workoutsFragment = new WorkoutsGridFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.layout_container, workoutsFragment);
		ft.addToBackStack("Workouts");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		Log.w(TAG, "Called showWorkouts()");
	}
	
	
	private class NavTabListener implements ActionBar.TabListener {
		public NavTabListener() {
		}
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (tab.equals(mTab1)){
				showWorkouts();
			}else if (tab.equals (mTab2)){
				showWeights();
			}
		}
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}
	}


	@Override
	public void OnWorkoutEntered(Workout workoutEntered, boolean isNewWorkout) {
		workoutsFragment.OnWorkoutEntered(workoutEntered, isNewWorkout);
	}
}
