package kmiddle2.nodes.areas;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import kmiddle2.log.NodeLog;
import kmiddle2.nodes.activities.ActivityConfiguration;
import kmiddle2.nodes.activities.Activity;
import kmiddle2.nodes.activities.ActivityAndType;

public abstract class Area {

	private AreaWrapper core;
	private ArrayList<ActivityAndType> process = new ArrayList<ActivityAndType>();
	protected int ID;
	protected Class<?> namer = null;
	protected NodeLog log;
	
	public int getID(){
		return ID;
	}
	
	public void setCore(AreaWrapper core){
		this.core = core;
	}
	
	public void setLog(NodeLog log){
		this.log = log;
	}
	
	protected void send(int toID, byte[] data){
		core.send(toID, data);
	}
	
	
	protected void send(int toID, int fromID, byte[] data){
		core.send(toID, fromID, data);
	}
	
	public ArrayList<ActivityAndType> getActivities(){
		return process;
	}
	
	protected void addProcess(Class<? extends Activity> className){ 
		addProcess(className, 0);
	}
	
	protected void addProcess(Class<? extends Activity> className, int configurationValue){
		addProcess(className, new ActivityConfiguration(configurationValue));
	}
	
	protected void addProcess(Class<? extends Activity> className, ActivityConfiguration nc){
		addProcess(className.getName(),nc);
	}
	
	
	protected void addProcess(String className){
		addProcess(className, 0);
	}
	
	protected void addProcess(String className, int configurationValue){
		addProcess(className, new ActivityConfiguration(configurationValue));
	}
	
	protected void addProcess(String className, ActivityConfiguration nc){
		this.process.add( new ActivityAndType(className,nc) );
	}
	
	public Class<?> getNamer(){
		return this.namer;
	}
	
	
	public void receive(int nodeID, byte[] data){}
	public byte[] process(int nodeID, byte[] data){return data;}
	
	private Class<?>[] parameterClasses;
	private Object[] parameterValues;
	public void setInitParameters(Class<?>[] classes, Object[] objects) {
		this.parameterClasses=classes;
		this.parameterValues=objects;
	}
	
	public void init(){
		Method m;
		try {
			m = this.getClass().getMethod("init", parameterClasses);
			m.invoke(this,parameterValues);
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Error getting init method");
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			System.out.println("Error invoking init method");
			e.printStackTrace();
		}
	}
}