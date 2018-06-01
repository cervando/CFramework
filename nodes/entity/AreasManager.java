package kmiddle2.nodes.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.p2p.EntityProtocols;
import kmiddle2.communications.routeTables.NodeRouteTable;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.nodes.areas.Area;
import kmiddle2.nodes.areas.AreaWrapper;

public class AreasManager {

	private NodeRouteTable routeTable;
	private ArrayList<AreaWrapper> areas;
	private EntityProtocols protocols;
	private NodeLog log;
	
	public AreasManager(NodeRouteTable routeTable, EntityProtocols protocols, NodeLog log){
		this.routeTable = routeTable;
		this.protocols = protocols;
		this.log = log;
		areas = new ArrayList<>();
	}
	
	public void addList(String[] areasNames, NodeConf nc){
		for ( int i = 0; i < areasNames.length; i++){
			add(areasNames[i],nc);
		}
		initALL();
	}
	
	public void addAndInit(String areaClassName, NodeConf nc){
		AreaWrapper area = add(areaClassName,nc);
		if ( area != null )
			area.init();
	}
	
	public AreaWrapper add(String areaClassName, NodeConf nc){
		log.developer("Adding area " + areaClassName + " to entity");
		Area area = getAreaObject(areaClassName);										//Iniciar
		if ( area == null )
			return null;
		
		//Add the area to a wrapper
		AreaWrapper core = new AreaWrapper(area,protocols,nc);
		core.setUp();
		
		//Add the wrapper to the list 
		areas.add(core);
		NodeAddress areaNode = core.getProtocol().getNodeAddress();
		routeTable.set(areaNode);
		
		core.setupActivities();
		return core;
	}
	
	public void initALL(){
		log.developer("Runing Area's init methods");
		for ( AreaWrapper area : areas){
			area.init();
		}
	}
	
	
	private Area getAreaObject(String areaClassConf){
		//Here you need to separate the className and the Arguments of the constructor
		String[] area = areaClassConf.split(":"); 
		String areaClassName = area[0];
		try {
			Class<?> nodeClass = Class.forName(areaClassName); //Conseguir clase
			Constructor<?> nodeConstr = nodeClass.getConstructor();
			Area a = (Area)nodeConstr.newInstance();
			if (area.length == 1 ) 
				return a;
			
			
			Class<?>[] parameterClasses = new Class<?>[(area.length -1)/2];
			Object[] parametersObjects = new Object[(area.length -1)/2];
			for ( int i = 1; i +1 < area.length; i+=2) {
				parameterClasses[(i-1)/2]  = Class.forName(area[i]);
				parametersObjects[(i-1)/2] = cast(parameterClasses[(i-1)/2], area[i+1]);
			}
			a.setInitParameters(parameterClasses, parametersObjects);
			return a;
			
			/*if (area.length == 1 ) {
				Constructor<?> nodeConstr = nodeClass.getConstructor();	
				return (Area)nodeConstr.newInstance();
			}else {
				Class<?>[] parameterClasses = new Class<?>[(area.length -1)/2];
				Object[] parametersObjects = new Object[(area.length -1)/2];
				for ( int i = 1; i +1 < area.length; i+=2) {
					parameterClasses[(i-1)/2]  = Class.forName(area[i]);
					parametersObjects[(i-1)/2] = cast(parameterClasses[(i-1)/2], area[i+1]);
				}
				
				try { 
					Constructor<?> nodeConstr = nodeClass.getConstructor(parameterClasses);
					return (Area)nodeConstr.newInstance(parametersObjects);
				} catch (NoSuchMethodException e) {
					/*
					StringBuilder sb = new StringBuilder();
					sb.append("(" + parameterClasses[0].getSimpleName());
					for( int i = 1; i< parameterClasses.length; i++) {
						sb.append("," + parameterClasses[i].getSimpleName());
					}
					sb.append(")");
					
					System.out.println("No existe constructor especificado para: " + areaClassName + "!!! \n\tREVISA QUE LOS PARAMETROS PROPORCIONADOS EXISTAN EN EL CONSTRUCTOR DE LA CLASE\n\t"+ sb.toString());
					
				}
				
			}*/
		} catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | ClassNotFoundException e) {
			log.error(e.toString()+ ": " + areaClassName);	
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
