package cFramework.util;

import java.lang.reflect.Field;

public class IDHelper {

	private static int AreaLeftShift = 32;
	
	//private static int AREAMASK = 0b111111111111 << AreaLeftShift;
	//private static int ACTIVITYMASK = 0b11111 << ActivityLeftShift;
	
	
	public static boolean isActivitiy(long ID){
		if((ID << AreaLeftShift) != 0)
			return true;
		return false;
	}
	
	
	public static boolean isArea(long ID){
		if((ID << AreaLeftShift) == 0 )
			return true;
		return false;
	}
	
	public static long getAreaID(long ID){
		return (long)((ID >> AreaLeftShift) << AreaLeftShift);
	}
	
	public static final long generateID(int AreaID, int ActivityID, int index){
		return  (((long)AreaID) << IDHelper.AreaLeftShift) + 
				(ActivityID);
	}
	
	public static final long generateID(String AreaName, int ActivityID, int index){
		return  (((long)AreaName.hashCode()) << AreaLeftShift )  +
				(ActivityID ) +
				index;
	}
	
	
	public static final long generateID(String areaName, String activityName){
		return  ((areaName.hashCode() << AreaLeftShift )) +
				activityName.hashCode();
	}
	
	
	
	public static String getNameAsString( Class<?> namer, long name ) {
		if ( namer == null )
				return String.valueOf(name);
		
		Field[] fields = namer.getFields();
		for ( int i = 0; i < fields.length; i++){
			try{
				if ( name == fields[i].getLong(null) )
					return fields[i].getName();
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return "";
		
		
	}
	
	
	/*public static String getAreaName( Class<?> namer, long name ) {
		long area = IDHelper.getAreaID(name);
		if ( namer == null )
			return String.valueOf(area);
		
		Field[] fields = namer.getFields();
		for ( int i = 0; i < fields.length; i++){
			try{
				if ( area == fields[i].getInt(null) )
					return fields[i].getName();
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return "";
	}
	
	
	
	public static String getTypeName( Class<?> namer, long name ){
		if ( namer == null )
			return String.valueOf((name ) >> AreaLeftShift);
		
		Field[] fields = namer.getFields();
		for ( int i = 0; i < fields.length; i++){
			try{
				if ( name == fields[i].getInt(null) ){
					String n =  fields[i].getName();
					int separatorIndex = n.indexOf("_");
					if ( separatorIndex != -1 ){
						return n.substring(separatorIndex + 1);
					}
				}
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return "";
	}*/
	
	
	
}
