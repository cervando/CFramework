package cFramework.nodes.area;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cFramework.communications.MessageMetadata;
import cFramework.communications.spikes.SpikeRouter;
import cFramework.log.NodeLog;
import cFramework.nodes.process.Process;
import cFramework.nodes.process.ProcessAndType;
import cFramework.nodes.process.ProcessConfiguration;

public abstract class Area {

	private AreaWrapper core;
	private ArrayList<ProcessAndType> process = new ArrayList<ProcessAndType>();
	protected long ID;
	protected Class<?> namer = null;
	protected NodeLog log;
	//protected Microglia microglia = new Microglia();
	
	
	
	
	MessageMetadata currentMetadata = null;
	protected boolean send(long toID, byte[] data){
		if ( currentMetadata != null)
			return send(toID, currentMetadata, data);
		else
			return send(toID, new MessageMetadata(0), data);
	}
	protected boolean send(long toID,MessageMetadata m,  byte[] data){
		//Send data Stimulates microglia
		return core.send(toID, m, data);
	}
	/*
	public boolean send(long toID, long fromID, byte[] data){
		//Send Data Stimules Microglia
		//micro
		//microglia.send();
		if ( currentMetadata != null)
			return send(toID, fromID, currentMetadata, data);
		else
			return send(toID, fromID, new MessageMetadata(0), data);
	}*/
	
	
	
	private boolean send(long toID, long fromID, MessageMetadata m, byte[] data){
		//Send Data Stimules Microglia
		//micro
		//microglia.send();
		return core.send(toID, fromID, m, data);
	}
	
	/*
	private void send(int toID, int fromID, byte[] data){
		if ( currentMetadata != null)
			core.send(toID, fromID, currentMetadata, data);
		else
			core.send(toID, fromID, new MessageMetadata(0), data);
	}*/
	
	
	
	
	
	public abstract void receive(long nodeID, byte[] data);
	
	
	public void process(long nodeID, byte[] data){
		//return data;
		this.receive(nodeID, data);
	}
	
	
	
	
	
	
	
	
	
	
	
	/***
	 * ALL Synchronization related Stuff
	 * 
	 * 
	 * 
	 */
	//THE first level is the ROUTERID, the second is the TIME, AND THE TIRDH IS THE SENDERID
	protected HashMap<Long, HashMap<Integer, HashMap<Long, byte[]>>> queueSpikes;
	
	//Routers for every Receiver
	protected HashMap<Long, List<SpikeRouter>> routes = new HashMap<Long, List<SpikeRouter>>();
	
	
	public void route(long sendTo, long fromID, MessageMetadata m, byte[] data){
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
						HashMap<Long, byte[]> spikeSet = queueSpikes.get(router.ROUTERID).get(m.time);
						//If needed, Allocate the space to save the Full Message
						if ( spikeSet == null ) {
							spikeSet = new HashMap<Long, byte[]>();
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
							for ( long  i: router.to ) {
								if ( i != ID)
									send( i, ID, m, spike);
								else
									//System.out.println("WHY WOULD YOU SEND IT TO YOURSELF, this will create an infinite Bucle");
									receive(fromID,  spike);
							}
							//Delete Full Message
							queueSpikes.get(router.ROUTERID).remove(m.time);
						}
					}
					
				//It is send to me to decide what to DO
				}else if ( sendTo == ID ){ 
					if ( router.merger == null ) {
						for ( long  i: router.to ) {
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
			queueSpikes = new HashMap<Long, HashMap<Integer, HashMap<Long, byte[]>>>();
		}
		
		r.ROUTERID = queueSpikes.size();
		queueSpikes.put(r.ROUTERID, new HashMap<Integer, HashMap<Long, byte[]>>());
		
		for ( long i: r.from ) {
			List<SpikeRouter> routesForThisSender = routes.get(i);
			if ( routesForThisSender == null ) {
				routesForThisSender = new ArrayList<SpikeRouter>();
				routes.put(i, routesForThisSender);
			}
			routesForThisSender.add(r);
		}
	}
	
	
	

	
	
	
	public long getID(){
		return ID;
	}
	
	public void setCore(AreaWrapper core){
		this.core = core;
	}
	
	public void setLog(NodeLog log){
		this.log = log;
	}
	
	public ArrayList<ProcessAndType> getActivities(){
		return process;
	}
	
	protected void addProcess(Class<? extends Process> className){ 
		addProcess(className, 0);
	}
	
	protected void addProcess(Class<? extends Process> className, int configurationValue){
		addProcess(className, new ProcessConfiguration(configurationValue));
	}
	
	protected void addProcess(Class<? extends Process> className, ProcessConfiguration nc){
		addProcess(className.getName(),nc);
	}
	
	
	protected void addProcess(String className){
		addProcess(className, 0);
	}
	
	protected void addProcess(String className, int configurationValue){
		addProcess(className, new ProcessConfiguration(configurationValue));
	}
	
	protected void addProcess(String className, ProcessConfiguration nc){
		this.process.add( new ProcessAndType(className,nc) );
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