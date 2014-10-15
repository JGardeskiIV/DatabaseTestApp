package edu.miracosta.cs134.databasetestapp;

//preconditions: all methods assume parameters have valid values (>=0 for ints)
	//i.e. error checking is done by dialogs, etc.
public class Workout {

	private long id; //unique id from database
	private String name;
	private int reps;
	private int sets;
	
	public Workout(long id, String name, int reps, int sets) {
		setId(id);
		setName(name);
		setReps(reps);
		setSets(sets);
	}
	//convenient constructor for when ID doesnt matter
	public Workout(String name, int reps, int sets) {
		setId(0);
		setName(name);
		setReps(reps);
		setSets(sets);
	}
	public Workout() {
		this(0, "None", 0, 0);
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setReps(int reps) {
		this.reps = reps;
	}
	public void setSets(int sets) {
		this.sets = sets;
	}
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getReps() {
		return reps;
	}
	public int getSets() {
		return sets;
	}
	
	public String toString() {
		return "Workout (" + id+ ") = " + name + " " + reps + "/" + sets;
	}
	
	public boolean equals(Object o) {
		if(o == null || o.getClass() != this.getClass()) {
			return false;
		} else {
			Workout w = (Workout)o;
			return w.id == this.id && w.name.equals(this.name) && w.reps == this.reps && w.sets == this.sets;
		}
	}
}
