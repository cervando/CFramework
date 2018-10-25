package kmiddle2.nodes.areas;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import kmiddle2.communications.LocalJVMNodeAddress;
import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.routeTables.SingletonNodeRouteTable;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.nodes.activities.ActivityConfiguration;
import kmiddle2.nodes.activities.Activity;
import kmiddle2.nodes.activities.ActivityAndType;
import kmiddle2.nodes.activities.ActivityWrapper;
import kmiddle2.util.OSHelper;

public class ActivityInitializer {

	private ArrayList<ActivityAndType> process = new ArrayList<ActivityAndType>();
	private NodeLog log = null;
	
	public ActivityInitializer(ArrayList<ActivityAndType> activities, NodeLog log){
		this.process = activities;
		this.log = log;
	}
	
	public void setUp(NodeAddress fatherAddress, NodeConf nc){
		for(ActivityAndType a:process){
			if ( a.getLenguage() == ActivityConfiguration.LENG_JAVA ){
				createJavaActivity(a, fatherAddress, nc);
				
			}else if ( a.getLenguage() == ActivityConfiguration.LENG_PYTHON ){
				createPythonActivity(a, fatherAddress, nc);
				
			}
		}
	}
	
	
	public void createJavaActivity(ActivityAndType a, NodeAddress fatherAddress, NodeConf nc){
		log.developer("Adding class " + a.getClassName());
		Activity activity = getActivityObject(a.getClassName());
		if (activity == null ){
			log.developer("Activity " + a.getClassName() + "Does not exist");
			return;
		}
		
		ActivityConfiguration pc;
		pc = a.getActivityConfiguration();
		pc.combine(nc.toInt());
		
		ActivityWrapper core = new ActivityWrapper(activity, fatherAddress, pc);						//Add the area to a wrapper
		activity.setCore(core);
		LocalJVMNodeAddress activityNode = new LocalJVMNodeAddress(core.getProtocol().getNodeAddress(), core);
		
		SingletonNodeRouteTable.getInstance().set(activityNode);
		a.setActivity(activity);
	}
	
	
	private Activity getActivityObject(String areaClassName){
		try {
			//Get the area intance from the string
			Class<?> nodeClass = Class.forName(areaClassName);										//Conseguir clase
			Constructor<?> nodeConstr = nodeClass.getConstructor();	
			return (Activity)nodeConstr.newInstance();
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			log.error(e.toString());	
		}
		return null;
	}
	
	public void createPythonActivity(ActivityAndType a, NodeAddress fatherAddress, NodeConf nc){
			
			String pythonLib = getPythonLibrary();
			if ( pythonLib == null){
				System.out.println("Python lib not finded, add it next to your middleware library");
				//return;
			}
				
			
			ActivityConfiguration pc;
			pc = new ActivityConfiguration(nc.toInt() | a.getActivityConfiguration().toInt());
			
			String command = "python ";
			command += OSHelper.preparePath(pythonLib + OSHelper.preparePathSegment("/node/activityWrapper.py")) + " ";
			command += a.getClassName() + " "; //classNameArgs;
			command += fatherAddress.getAddress().getIp()+":"+ fatherAddress.getAddress().getPort() + " ";//fatherArgs;
			command += pc.toInt() + " "; //optionsArgs;
			command += OSHelper.preparePath(System.getProperty("java.class.path"));
			//command += namerArgs;
			System.out.println(command);
			OSHelper.exec(command);
	}
	
	private String getPythonLibrary(){
		String classPath = System.getProperty("java.class.path");
		String[] paths = classPath.split(File.pathSeparator);
		for ( String p:paths){
			File f = new File(p);
			if ( !f.isDirectory() )
				f = f.getParentFile();
			f = new File(f.getAbsolutePath() + OSHelper.preparePathSegment("/middlewarePython"));
			if ( f.exists() )
				return f.getAbsolutePath();
			
		}
		return null;
	}
	
	public void initAll(){
		//NodeRouteTable routeTable = SingletonNodeRouteTable.getInstance();
		for(final ActivityAndType a:process){
			if ( a.getLenguage() == ActivityConfiguration.LENG_JAVA ){
				new Thread(){
					public void run(){
						a.getActivity().init();
					}
				}.start();
				
			}else if ( a.getLenguage() == ActivityConfiguration.LENG_PYTHON ){
				//Send init to Activity python
				//NodeAddress na = routeTable.get(a.getID());																		
			}
		}
	}
}
