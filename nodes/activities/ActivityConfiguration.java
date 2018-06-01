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
package kmiddle2.nodes.activities;

import kmiddle2.nodes.NodeConf;

public class ActivityConfiguration extends NodeConf {

	private final int MASK_TYPE				= 	0b00000000000000000000000011100000;
	public static final int TYPE_SINGLETON 	=	0b00000000000000000000000001000000;
	public static final int TYPE_PARALLEL	=	0b00000000000000000000000000100000;
	
	private final int MASK_LENG				= 	0b00000000000000000000011100000000;
	public static final int LENG_JAVA 		=	0b00000000000000000000001000000000;
	public static final int LENG_PYTHON 	=	0b00000000000000000000000100000000;
	
	
	public ActivityConfiguration(){
		this(0);
	}
	
	public ActivityConfiguration(int val){
		this(val, (byte)0 );
	}
	
	public ActivityConfiguration(int val, byte entityID){
		super(val, entityID);
	}
	

	public void setType(int mode){
		val = (val & ~MASK_TYPE) | mode;
	}
	
	public int getType(){
		int ret = val & MASK_TYPE;
		if ( ret == 0 ) ret = TYPE_SINGLETON;
		return ret;
	}
	
	
	public void setLenguage(int leng){
		val = (val & ~MASK_LENG) | leng;
	}
	
	public int getLenguage(){
		int ret = val & MASK_LENG;
		if ( ret == 0 ) ret = LENG_JAVA;
		return ret;
	}
}