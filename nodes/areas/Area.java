package kmiddle2.nodes.areas;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import kmiddle2.log.NodeLog;
import kmiddle2.nodes.activities.ActConf;
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
		addProcess(className, new ActConf(configurationValue));
	}
	
	protected void addProcess(Class<? extends Activity> className, ActConf nc){
		addProcess(className.getName(),nc);
	}
	
	
	protected void addProcess(String className){
		addProcess(className, 0);
	}
	
	protected void addProcess(String className, int configurationValue){
		addProcess(className, new ActConf(configurationValue));
	}
	
	protected void addProcess(String className, ActConf nc){
		this.process.add( new ActivityAndType(className,nc) );
	}
	
	public Class<?> getNamer(){
		return this.namer;
	}
	
	
	public void receive(int nodeID, byte[] data){}
	public byte[] process(int nodeID, byte[] data){return data;}
	
	private Class<?>[] c;
	private Object[] o;
	public void setInitParameters(Class<?>[] c, Object[] o) {
		this.c=c;
		this.o=o;
	}
	
	public void init(){
		Method m;
		try {
			m = this.getClass().getMethod("init", c);
			m.invoke(this,o);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			StringBuilder sb = new StringBuilder();
			sb.append("(" + c[0].getSimpleName());
			for( int i = 1; i< c.length; i++) {
				sb.append("," + c[i].getSimpleName());
			}
			sb.append(")");
			
			System.out.println("Error geting Init method\n\tREVISA QUE LOS PARAMETROS PROPORCIONADOS EXISTAN EN EL CONSTRUCTOR DE LA CLASE\n\t"+ sb.toString());
			e.printStackTrace();			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			System.out.println("Error invoking init method");
			e.printStackTrace();
		}
	}
}