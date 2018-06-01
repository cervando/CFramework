package kmiddle2.nodes.activities;

public class ActivityAndType {

	private Activity activity;
	private int id;
	private String className;
	private ActConf pc;
	
	public ActivityAndType(String className, ActConf pc){
		this.className = className;
		this.pc = pc;
	}
	
	public int getLenguage(){
		return pc.getLenguage();
	}
	
	public String getClassName(){
		return className;
	}
	
	public ActConf getActivityConfiguration(){
		return pc;
	}
	
	public void setActivity(Activity a){
		this.activity = a;
		id = a.getID();
	}
	
	public Activity getActivity(){
		return activity;
	}
	
	public void setID(int a){
		this.id = a;
	}
	
	public int getID(){
		return id;
	}
}