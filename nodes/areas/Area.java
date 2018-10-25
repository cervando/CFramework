package kmiddle2.nodes.areas;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kmiddle2.communications.MessageMetadata;
import kmiddle2.communications.spikes.SpikeRouter;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.activities.Activity;
import kmiddle2.nodes.activities.ActivityAndType;
import kmiddle2.nodes.activities.ActivityConfiguration;

public abstract class Area {

	private AreaWrapper core;
	private ArrayList<ActivityAndType> process = new ArrayList<ActivityAndType>();
	protected int ID;
	protected Class<?> namer = null;
	protected NodeLog log;
	
	
	
	
	MessageMetadata currentMetadata = null;
	protected void send(int toID, byte[] data){
		if ( currentMetadata != null)
			send(toID, currentMetadata, data);
		else
			send(toID, new MessageMetadata(0), data);
	}
	protected void send(int toID,MessageMetadata m,  byte[] data){
		core.send(toID, m, data);
	}
	
	/*
	private void send(int toID, int fromID, byte[] data){
		if ( currentMetadata != null)
			core.send(toID, fromID, currentMetadata, data);
		else
			core.send(toID, fromID, new MessageMetadata(0), data);
	}*/
	
	private void send(int toID, int fromID, MessageMetadata m, byte[] data){
		core.send(toID, fromID, m, data);
	}
	
	
	
	public void receive(int nodeID, byte[] data){}
	public byte[] process(int nodeID, byte[] data){return data;}
	
	
	
	
	//THE first level is the ROUTERID, the second is the TIME, AND THE TIRDH IS THE SENDERID
	protected HashMap<Integer, HashMap<Integer, HashMap<Integer, byte[]>>> queueSpikes;
	
	//Routers for every Receiver
	protected HashMap<Integer, List<SpikeRouter>> routes = new HashMap<Integer, List<SpikeRouter>>();
	
	
	
	public void route(int sendTo, int fromID, MessageMetadata m, byte[] data){
		currentMetadata = m;
		List<SpikeRouter> routesForThisSender = routes.get(fromID);
		
		//I got no routers for this, I will Send it where the package SAIS it will GO
		if ( routesForThisSender == null ) {
			//IT IS TO ME
			if ( sendTo == ID ) 
				receive(fromID,  data);
			//IT IS TO someone else
			else
				send(sendTo,fromID, m, data);
			
		//I got Routers For this 
		}else {
			
			//For every Router that has this sender as input
			for (SpikeRouter router:routesForThisSender) {
				
				//This router has the same Target that this Message
				if (router.isTargetToID(sendTo) ) {
					
					//This has no Merging procedure, so, is a single message
					if ( router.merger == null ) {
						//for ( int  i: router.to
						send(sendTo,fromID, m, data);
						
					//Is a fragment of a message from diferent areas
					}else {
						//Get the Full Message Reserved Area
						HashMap<Integer, byte[]> spikeSet = queueSpikes.get(router.ROUTERID).get(m.time);
						//If needed, Allocate the space to save the Full Message
						if ( spikeSet == null ) {
							spikeSet = new HashMap<Integer, byte[]>();
							queueSpikes.get(router.ROUTERID).put(m.time, spikeSet );
						}
						//Save this fragment in the full message
						spikeSet.put(fromID, data);
						
						//Testing Proposes
						//spikeSet = queueSpikes.get(router.ROUTERID).get(m.time);
						//System.out.println("ADDED DATA TO QUEUE SIZE:"+ spikeSet.size() +" TO ROUTER:" + router.ROUTERID + " TIME:" + m.time + " FROM:" + fromID);
						
						
						//Check If the spike Set if complete
						if ( spikeSet.size() == router.from.length ) {
							//If it is Combine all the information
							byte[] spike = router.merger.merge(spikeSet);
							//Send it to every RECEIVER
							for ( int  i: router.to ) {
								if ( i != ID)
									send( i, ID, m, spike);
								else
									System.out.println("WHY WOULD YOU SEND IT TO YOURSELF, this will create an infinite Buckle");
							}
							//Delete Full Message
							queueSpikes.get(router.ROUTERID).remove(m.time);
						}
					}
					
				//It is send to me to decide what to DO
				}else if ( sendTo == ID ){ 
					if ( router.merger == null ) {
						for ( int  i: router.to ) {
							if ( i != ID)
								send( i, ID, m, data);
							else
								System.out.println("WHY WOULD YOU SEND IT TO YOURSELF, this will create an infinite Buckle");
						}
					}
				}
			}
		}
	}
	
	
	public void AddRoute(SpikeRouter r) {
		//THE first level is the ROUTERID, the second is the TIME, AND THE TIRDH IS THE SENDERID
		//HashMap<Integer, HashMap<Integer, HashMap<Integer, List<byte[]>>>> queueSpikes
		if ( queueSpikes == null ) {
			queueSpikes = new HashMap<Integer, HashMap<Integer, HashMap<Integer, byte[]>>>();
		}
		
		r.ROUTERID = queueSpikes.size();
		queueSpikes.put(r.ROUTERID, new HashMap<Integer, HashMap<Integer, byte[]>>());
		
		for ( int i: r.from ) {
			List<SpikeRouter> routesForThisSender = routes.get(i);
			if ( routesForThisSender == null ) {
				routesForThisSender = new ArrayList<SpikeRouter>();
				routes.put(i, routesForThisSender);
			}
			routesForThisSender.add(r);
		}
	}
	
	
	

	
	
	
	public int getID(){
		return ID;
	}
	
	public void setCore(AreaWrapper core){
		this.core = core;
	}
	
	public void setLog(NodeLog log){
		this.log = log;
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
			if ( !m.toString().contains(Area.class.getName()) )
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