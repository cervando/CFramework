package cFramework.nodes.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import cFramework.communications.NodeAddress;
import cFramework.communications.p2p.EntityProtocols;
import cFramework.communications.routeTables.NodeRouteTable;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import cFramework.nodes.routers.Router;
import cFramework.nodes.routers.RouterWrapper;

public class AreasManager {

	private NodeRouteTable routeTable;
	private ArrayList<RouterWrapper> areas;
	private EntityProtocols protocols;
	private NodeLog entityLog;
	
	
	/**
	 * AreasManager Constructor 
	 * @param routeTable 	Global route table for this entity
	 * @param protocols		Communication protocols for the entity
	 * @param log			Log for the entity
	 */
	public AreasManager(NodeRouteTable routeTable, EntityProtocols protocols, NodeLog log){
		this.routeTable = routeTable;
		this.protocols = protocols;
		this.entityLog = log;
		areas = new ArrayList<>();
	}
	
	
	
	/**
	 * Adds a list of areas and init all after that
	 * @param areasNames	List of areas
	 * @param nc			configuration for the entity
	 */
	public void addList(String[] areasNames, NodeConf nc){
		for ( int i = 0; i < areasNames.length; i++){
			add(areasNames[i],nc);
		}
		//Call the init method for all the areas
		initAllAreas();
	}
	
	
	
	/**
	 * Add One Area and ignite it 
	 * @param areaClassName
	 * @param nc
	 */
	public void addAndInit(String areaClassName, NodeConf nc){
		RouterWrapper area = add(areaClassName,nc);
		if ( area != null )
			area.init();
	}
	
	
	
	/**
	 * Instantiate and add One Area
	 * @param areaClassName	Class name for the area 
	 * @param nc			Node configuration for the area
	 * @return	Instantiated Area Object
	 */
	public RouterWrapper add(String areaClassName, NodeConf nc){
		entityLog.developer("Adding area " + areaClassName + " to entity");
		Router area = getAreaObject(areaClassName);										//Iniciar
		if ( area == null )
			return null;
		
		//Add the area to a wrapper
		RouterWrapper core = new RouterWrapper(area,protocols,nc);
		core.setUp();
		
		//Add the wrapper to the list 
		areas.add(core);
		NodeAddress areaNode = core.getProtocol().getNodeAddress();
		routeTable.set(areaNode);
		
		core.setupActivities();
		return core;
	}
	
	public void initAllAreas(){
		entityLog.developer("Runing Area's init methods");
		for ( RouterWrapper area : areas){
			area.init();
		}
	}
	
	
	/**
	 * Instantiate a Area class
	 * @param areaClassConf		area to be instantiate Format:
	 * areaName:parameter 1 class:parameter 1 value: ... :parameter N class:parameter N value
	 * @return Instantiated Area object or null if an error happened
	 */
	private Router getAreaObject(String areaClassConf){
		//Here you need to separate the className and the Arguments of the constructor
		String[] area = areaClassConf.split(":"); 
		String areaClassName = area[0];
		try {
			Class<?> nodeClass = Class.forName(areaClassName); //Conseguir clase
			Constructor<?> nodeConstr = nodeClass.getConstructor();
			Router a = (Router)nodeConstr.newInstance();
			if (area.length == 1 ) 
				return a;
			
			//Get the Classes and values of the parameters
			Class<?>[] parameterClasses = new Class<?>[(area.length -1)/2];
			Object[] parametersObjects = new Object[(area.length -1)/2];
			for ( int i = 1; i +1 < area.length; i+=2) {
				parameterClasses[(i-1)/2]  = Class.forName(area[i]);
				parametersObjects[(i-1)/2] = cast(parameterClasses[(i-1)/2], area[i+1]);
			}
			
			
			try {
				a.getClass().getMethod("init", parameterClasses);
				a.setInitParameters(parameterClasses, parametersObjects);
				return a;
			//Theres not exist a init method with those parameters
			} catch (NoSuchMethodException | SecurityException e) {
				StringBuilder sb = new StringBuilder(  parameterClasses[0].getSimpleName() );
				for( int i = 1; i< parameterClasses.length; i++) {
					sb.append("," + parameterClasses[i].getSimpleName());
				}
				System.out.println("ERROR there is no method init("+ sb.toString() + ") on class " + areaClassName);
			}
			
		} catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | ClassNotFoundException e) {
			entityLog.error(e.toString()+ ": " + areaClassName);	
			e.printStackTrace();
		} 
		return null;
	}
	
	
	
	public Object cast(Class<?> goalClass, String object) {
		if( goalClass.equals(Byte.class))
			return Byte.parseByte(object);
		if( goalClass.equals(Short.class))
			return Short.parseShort(object);
		if( goalClass.equals(Integer.class))
			return Integer.parseInt(object);
		if( goalClass.equals(Long.class))
			return Float.parseFloat(object);
		if( goalClass.equals(Float.class))
			return Float.parseFloat(object);
		if( goalClass.equals(Double.class))
			return Double.parseDouble(object);
		if( goalClass.equals(Boolean.class))
			return Boolean.parseBoolean(object);
		if( goalClass.equals(Character.class))
			return object.charAt(0);
		if( goalClass.equals(String.class))
			return object;
		return null;
	}
}
