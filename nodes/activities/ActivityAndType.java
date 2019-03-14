package cFramework.nodes.activities;

public class ActivityAndType {

	private Activity activity;
	private long id;
	private String className;
	private ActivityConfiguration pc;
	
	public ActivityAndType(String className, ActivityConfiguration pc){
		this.className = className;
		this.pc = pc;
	}
	
	public int getLenguage(){
		return pc.getLenguage();
	}
	
	public String getClassName(){
		return className;
	}
	
	public ActivityConfiguration getActivityConfiguration(){
		return pc;
	}
	
	public void setActivity(Activity a){
		this.activity = a;
		id = a.getID();
	}
	
	public Activity getActivity(){
		return activity;
	}
	
	public void setID(long a){
		this.id = a;
	}
	
	public long getID(){
		return id;
	}
}