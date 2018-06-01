	package kmiddle2.util;

import java.io.IOException;

public class OSHelper {

	
	public static Process exec(String command){
		String OS = System.getProperty("os.name", "generic").toLowerCase();
		if ((OS.contains("mac") ) || (OS.contains("darwin") )) {
			try {
		        final ProcessBuilder processBuilder = new ProcessBuilder(
		                    "/usr/bin/osascript",
		                    "-e", "tell app \"Terminal\"",
		                    "-e", "do script \"" + command + "\"",
		                    "-e", "end tell");
		        return processBuilder.start();
		    } catch (IOException e) {
				e.printStackTrace();
			}
			
		} else if (OS.contains("win") ) {
			try {
				return Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start " + command});
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else if (OS.contains("nux") ){
			try {
				return Runtime.getRuntime().exec(new String[]{"bash", "-c", "gnome-terminal -x " + command});
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public static String preparePathSegment(String path){
		String OS = System.getProperty("os.name", "generic").toLowerCase();	
		if (OS.contains("win") ) {
			return path.replace("/", "\\");
					
		} else if (OS.contains("nux") ){
			return path.replace(" ", "\\ ");
			
		} else if ((OS.contains("mac") ) || (OS.contains("darwin") )) {
			return path.replace(" ", "\\\\ ");
				
		}
		return null;
	}
	
	
	public static String preparePath(String path){
		String OS = System.getProperty("os.name", "generic").toLowerCase();	
		if (OS.contains("win") ) {
			return "\"" + path.replace("/", "\\") + "\"";
					
		} else if (OS.contains("nux") ){
			return path.replace(" ", "\\ ");
			
		} else if ((OS.contains("mac") ) || (OS.contains("darwin") )) {
			return path.replace(" ", "\\\\ ");
				
		}
		return null;
	}
	
	
	public static String getMainFolder(){
		String[] dir = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		for (int i=0; i < dir.length;i++){
			if ( dir[i].endsWith("/build/classes") || dir[i].endsWith("\\build\\classes") )
				return dir[i].substring(0, dir[i].length() - "/build/classes".length());
			if ( dir[i].endsWith("/bin") || dir[i].endsWith("\\bin"))
				return dir[i].substring(0, dir[i].length() - "/bin".length());
		}
		return dir[dir.length -1];
		
	}
}

	