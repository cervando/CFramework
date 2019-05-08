package cFramework.log;

/**
 * Kuayolotl Middleware System
 * @author Karina Jaime <ajaime@gdl.cinvestav.mx>
 *  
 * This file is part of Kuayolotl Middleware
 *
 * Kuayolotl Middleware is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *	
 * Kuayolotl Middleware is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *	
 * You should have received a copy of the GNU General Public License
 * along with Kuayolotl Middleware.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import cFramework.communications.fiels.Address;
import cFramework.util.IDHelper;
import cFramework.util.OSHelper;



/*
 * 
 * 
The Loggler class has the next levels 
    SEVERE (highest)
    WARNING
    INFO
    CONFIG
    FINE
    FINER
    FINEST
 * 
 * 
 */





public class NodeLog{
	
	LogRegisterAble register;
	Logger logger = null; 
	Boolean isDebug;
	String className;
	
	
	
	public NodeLog(String name, Boolean isDebug){
		
		register = new AreaRegistrer();
		logger = Logger.getLogger(name);
		logger.setUseParentHandlers(false);
		ConsoleHandler ch =  new ConsoleHandler();		
		ch.setFormatter(new ConsoleFormatter(name));
		
		
		if ( isDebug == null ){
			logger.setLevel(Level.ALL);
			ch.setLevel(Level.ALL);
		}else if ( isDebug ){
			logger.setLevel(Level.FINER);
			ch.setLevel(Level.FINER);
		}else{
			logger.setLevel(Level.FINE);
			ch.setLevel(Level.FINE);
		}
		
		logger.addHandler(ch);
	}
	
	
	public NodeLog(long name, Class<?> namer, Boolean isDebug ){
		
		this.isDebug = isDebug;
		if ( IDHelper.isArea(name) )
			register = new AreaRegistrer(namer);
		else
			register = new ActivityRegister(namer);
		
		className = IDHelper.getNameAsString(namer, name);
		logger = Logger.getLogger(className);
		logger.setUseParentHandlers(false);
		ConsoleHandler ch =  new ConsoleHandler();														//Set conole formatter
		ch.setFormatter(new ConsoleFormatter(className));
		
		if ( isDebug == null ){
			logger.setLevel(Level.ALL);
			ch.setLevel(Level.ALL);
		}else if ( isDebug ){
			logger.setLevel(Level.FINER);
			ch.setLevel(Level.FINER);
		}else{
			logger.setLevel(Level.FINE);
			ch.setLevel(Level.FINE);
		}
		
		
		logger.addHandler(ch);
		
		String path = OSHelper.getMainFolder() + "/log";												//Create Log Folder if does not exist
		if (! new File(path).exists())
			if (! new File(path).mkdir() ){				
				System.out.println("Fatal error Could not create log folder");
				System.out.println(path);
			}
		try {																							//Format XML file
			Calendar cal = Calendar.getInstance();
			//Name of the LOG file
			//if ( isDebug == null )
			//	System.out.println("Creating Log file on: " + path + "/" + cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.MONTH) + "_" + cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.HOUR_OF_DAY) + "_" + cal.get(Calendar.MINUTE) + "_" + className + ".log");
			FileHandler fh = new FileHandler(path + "/" + cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.MONTH) + "_" + 
										cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.HOUR_OF_DAY) + "_" + cal.get(Calendar.MINUTE) + "_" + className + ".log");
			logger.addHandler(fh);
			fh.setEncoding("UTF-8");
			fh.setFormatter(new XMLFileFormatter());	
		} catch (SecurityException | IOException e) {
			logger.warning(e.getMessage());
		}
	}
	
	

	
	public void header(long name, Address address) {
		logger.severe(register.header(name, address));
	}


	public void send(long name, String more) {
		logger.severe(register.send(name, more));
	}
	
	public void send_debug(long name, String more){
		logger.finer(register.send(name, more));
	}
	
	public void send(long name, short type, String more) {
		logger.severe(register.send(name, more));
	}

	
	public void send(Address addr, String more) {
		logger.info(register.send(addr, more));
	}

	
	public void receive(long name, String more) {
		logger.info(register.receive(name, more));
	}

	
	public void receive(Address addr, String more) {
		logger.info(register.receive(addr, more));
	}
	
	public void receive(long name, short type, String more) {
		logger.severe(register.receive(name, more));
	}
	
	public void receive_debug(long name, String more) {
		logger.finer(register.receive_debug(name, more));
	}
	
	
	public void saveRequest(long name, String dataType) {
		logger.info(register.saveRequest(name, dataType));
	}

	
	
	public void debug(String more) {
		logger.finer(register.info(more));
	}
	
	public void debug(String more, long to) {
		logger.finer(register.info(more, to));
	}
	
	
	
	
	public void developer(String more){
		logger.finest(register.developer(more));
	}
	
	
	public void developer( String more, long to){
		logger.finest(register.developer(more, to));
	}
	
	
	
	
	public void error(String more) {
		logger.severe(register.error(more));
	}
	
	public void message(String message){
		logger.severe(register.info(message));
	}
	
	/*
	public void config( String more) {
		logger.config(register.info( more));
	}

	
	public void info(String more) {
		logger.info(register.info(more));
	}
	*/
	
}
