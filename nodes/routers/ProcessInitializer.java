package cFramework.nodes.routers;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import cFramework.communications.LocalJVMNodeAddress;
import cFramework.communications.NodeAddress;
import cFramework.communications.routeTables.SingletonNodeRouteTable;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import cFramework.nodes.processes.Process;
import cFramework.nodes.processes.ProcessAndType;
import cFramework.nodes.processes.ProcessConfiguration;
import cFramework.nodes.processes.ProcessWrapper;
import cFramework.util.OSHelper;

public class ProcessInitializer {

	private ArrayList<ProcessAndType> process = new ArrayList<ProcessAndType>();
	private NodeLog log = null;
	
	public ProcessInitializer(ArrayList<ProcessAndType> activities, NodeLog log){
		this.process = activities;
		this.log = log;
	}
	
	public void setUp(NodeAddress fatherAddress, NodeConf nc){
		for(ProcessAndType a:process){
			if ( a.getLenguage() == ProcessConfiguration.LENG_JAVA ){
				createJavaActivity(a, fatherAddress, nc);
				
			}else if ( a.getLenguage() == ProcessConfiguration.LENG_PYTHON ){
				createPythonActivity(a, fatherAddress, nc);
				
			}
		}
	}
	
	
	public void createJavaActivity(ProcessAndType a, NodeAddress fatherAddress, NodeConf nc){
		log.developer("Adding class " + a.getClassName());
		Process activity = getActivityObject(a.getClassName());
		if (activity == null ){
			log.developer("Activity " + a.getClassName() + "Does not exist");
			return;
		}
		
		ProcessConfiguration pc;
		pc = a.getActivityConfiguration();
		pc.combine(nc.toInt());
		
		ProcessWrapper core = new ProcessWrapper(activity, fatherAddress, pc);						//Add the area to a wrapper
		activity.setCore(core);
		LocalJVMNodeAddress activityNode = new LocalJVMNodeAddress(core.getProtocol().getNodeAddress(), core);
		
		SingletonNodeRouteTable.getInstance().set(activityNode);
		a.setActivity(activity);
	}
	
	
	private Process getActivityObject(String areaClassName){
		try {
			//Get the area intance from the string
			Class<?> nodeClass = Class.forName(areaClassName);										//Conseguir clase
			Constructor<?> nodeConstr = nodeClass.getConstructor();	
			return (Process)nodeConstr.newInstance();
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			log.error(e.toString());	
		}
		return null;
	}
	
	public void createPythonActivity(ProcessAndType a, NodeAddress fatherAddress, NodeConf nc){
			
			String pythonLib = getPythonLibrary();
			if ( pythonLib == null){
				System.out.println("Python lib not finded, add it next to your middleware library");
				//return;
			}
				
			
			ProcessConfiguration pc;
			pc = new ProcessConfiguration(nc.toInt() | a.getActivityConfiguration().toInt());
			
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
		for(final ProcessAndType a:process){
			if ( a.getLenguage() == ProcessConfiguration.LENG_JAVA ){
				new Thread(){
					public void run(){
						a.getActivity().init();
					}
				}.start();
				
			}else if ( a.getLenguage() == ProcessConfiguration.LENG_PYTHON ){
				//Send init to Activity python
				//NodeAddress na = routeTable.get(a.getID());																		
			}
		}
	}
}
