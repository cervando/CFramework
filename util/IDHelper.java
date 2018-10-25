package kmiddle2.util;

import java.lang.reflect.Field;

public class IDHelper {

	private static int AreaLeftShift = 20;
	private static int ActivityLeftShift = 15;
	
	private static int AREAMASK = 0b111111111111 << AreaLeftShift;
	private static int ACTIVITYMASK = 0b11111 << ActivityLeftShift;
	
	
	public static boolean isActivitiy(int ID){
		if((ID & IDHelper.ACTIVITYMASK) != 0)
			return true;
		return false;
	}
	
	public static boolean isArea(int ID){
		if((ID & IDHelper.AREAMASK) == ID)
			return true;
		return false;
	}
	
	public static int getAreaID(int ID){
		return ID & IDHelper.AREAMASK;
	}
	
	public static final int generateID(int AreaID, int ActivityID, int index){
		return  ((AreaID << IDHelper.AreaLeftShift) & AREAMASK) + 
				((ActivityID << IDHelper.ActivityLeftShift) & ACTIVITYMASK) + 
				index;
	}
	
	public static final int generateID(String AreaName, int ActivityID, int index){
		return  ((AreaName.hashCode() << AreaLeftShift ) & AREAMASK) +
				((ActivityID << IDHelper.ActivityLeftShift) & ACTIVITYMASK) +
				index;
	}
	
	public static String getNameAsString( Class<?> namer, int name ) {
		if ( namer == null )
				return String.valueOf(name);
		String areaName = getAreaName(namer, name);
		String typeName = getTypeName(namer, name);
		//String index = String.valueOf(name & SMALLNODEMASK);
		//if( typeName.equals("") && index.equals("0"))
		if( typeName.equals("") )
			return areaName;
		return areaName + "_" + typeName;// + "_" + index;
	}
	
	
	public static String getAreaName( Class<?> namer, int name ) {
		int area = IDHelper.getAreaID(name);
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
	
	public static String getTypeName( Class<?> namer, int name ){
		if ( namer == null )
			return String.valueOf((name & IDHelper.ACTIVITYMASK) >> IDHelper.ActivityLeftShift);
		
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
	}
	
	
	
}
