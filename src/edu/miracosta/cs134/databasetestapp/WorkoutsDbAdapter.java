package edu.miracosta.cs134.databasetestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorkoutsDbAdapter {

	/* START code for DatabaseHelper = sets up database */
	public static final String KEY_ROWID = "_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_REPS = "reps";
	public static final String FIELD_SETS = "sets";
	public static final String[] WORKOUT_FIELDS = new String[] {
		KEY_ROWID,
		FIELD_NAME,
		FIELD_REPS,
		FIELD_SETS
	};
	
	public static final String TAG = WorkoutsDbAdapter.class.getSimpleName(); //used for Log.w()
	private static final String DATABASE_NAME = "FITNESS_APP";
	private static final String DATABASE_TABLE = "workout";
	private static final int DATABASE_VERSION = 1; //increment if changes made to DB
	
	//SQL statement for creating the database per our model
		// _id is the primary key
	//CREATE TABLE workout (_id INTEGER PRIMARY KEY AUTOINCREMENT, name text, reps INTEGER, sets INTEGER);
	private static final String DATABASE_CREATE = 
			"CREATE TABLE " + DATABASE_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FIELD_NAME + " text,"
			+ FIELD_REPS + " INTEGER,"
			+ FIELD_SETS + " INTEGER"
			+ ");";
	
	private final Context mCtx; //Interface to global information about an application environment
	private DatabaseHelper mDbHelper;
	SQLiteDatabase mDb;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			Log.w(TAG, "Built DatabaseHelper");
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			Log.w(TAG, "onCreate() called, now we have a DB!");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading databas from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			//could also backup the data, or transmute it to fit the new version.
			onCreate(db);
		}
	}
	
	/* END code for DatabaseHelper */

	/* START code for WorkoutsDbAdapter = Java interface to access/modify data*/
	
	public WorkoutsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public WorkoutsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		if(mDbHelper != null) {
			mDbHelper.close();
		}
	}
	
	public void upgrade() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		mDbHelper.onUpgrade(mDb, 1, 0);
	}
	
	
	//methods to actually do things to the DB
	
	//returns id of inserted row
	public long createWorkout(Workout workoutToAdd) {
		ContentValues initialValues = new ContentValues(); //holds values for query
		//ignoring id in workout since DB will handle key to this!
		if(workoutToAdd.getName() != null) {
			initialValues.put(FIELD_NAME, workoutToAdd.getName());
		}
		if(workoutToAdd.getReps() > 0) {
			initialValues.put(FIELD_REPS, workoutToAdd.getReps());
		}
		if(workoutToAdd.getSets() > 0) {
			initialValues.put(FIELD_SETS, workoutToAdd.getSets());
		}
		return mDb.insert(DATABASE_TABLE, null, initialValues); //perform insert query, returns -1 if error or id
	}
	
	public boolean updateWorkout(Workout workoutToUpdate) {
		ContentValues updateValues = new ContentValues();
		
		Log.w(TAG, "ID for update = " + workoutToUpdate.getId());
		if(workoutToUpdate.getId() > 0) {
			updateValues.put(KEY_ROWID, workoutToUpdate.getId());
		}
		if(workoutToUpdate.getName() != null) {
			updateValues.put(FIELD_NAME, workoutToUpdate.getName());
		}
		if(workoutToUpdate.getReps() > 0) {
			updateValues.put(FIELD_REPS, workoutToUpdate.getReps());
		}
		if(workoutToUpdate.getSets() > 0) {
			updateValues.put(FIELD_SETS, workoutToUpdate.getSets());
		}
		
		String[] args = new String[]{ Long.toString(workoutToUpdate.getId()) };
		Log.w(TAG, "Updated values = " + updateValues.toString());
		int result = mDb.update(DATABASE_TABLE, updateValues, KEY_ROWID + " = ?", args);
		Log.w(TAG, "Result from update query = " + result);
		return result > 0;
	}
	
	public boolean deleteWorkout(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteWorkout(String name) {
		return mDb.delete(DATABASE_TABLE, FIELD_NAME + "=" + name, null) > 0;
	}
	
	public Cursor fetchWorkouts() {
		return mDb.query(DATABASE_TABLE, WORKOUT_FIELDS, null, null, null, null, null);
	}
	
	//useful method to help with above one! that way we don't deal with
	// very little database stuff! just our model (Workout)!
	//assume cursor is at appropriate row
	public static Workout getWorkoutFromCursor(Cursor cursor) {
		Workout result;
		long id = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
		String name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
		int reps = cursor.getInt(cursor.getColumnIndex(FIELD_REPS));
		int sets = cursor.getInt(cursor.getColumnIndex(FIELD_SETS));
		result = new Workout(id, name, reps, sets);
		Log.w(TAG, "cursor says workout = " + result.toString());
		return result;
	}

	/* END code for WorkoutsDbAdapter */

}
