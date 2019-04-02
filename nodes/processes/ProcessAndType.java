package cFramework.nodes.processes;

public class ProcessAndType {

	private Process activity;
	private long id;
	private String className;
	private ProcessConfiguration pc;
	
	public ProcessAndType(String className, ProcessConfiguration pc){
		this.className = className;
		this.pc = pc;
	}
	
	public int getLenguage(){
		return pc.getLenguage();
	}
	
	public String getClassName(){
		return className;
	}
	
	public ProcessConfiguration getActivityConfiguration(){
		return pc;
	}
	
	public void setActivity(Process a){
		this.activity = a;
		id = a.getID();
	}
	
	public Process getActivity(){
		return activity;
	}
	
	public void setID(long a){
		this.id = a;
	}
	
	public long getID(){
		return id;
	}
}