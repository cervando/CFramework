package cFramework.communications.routeTables;

public class SingletonNodeRouteTable extends NodeRouteTable{

	private static SingletonNodeRouteTable instance = null;
	
	public static SingletonNodeRouteTable getInstance(){
		if ( instance == null ){
			instance = new SingletonNodeRouteTable();
		}
		return instance;
	}
}
