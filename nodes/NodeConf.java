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

package kmiddle2.nodes;

public class NodeConf {
	
	//protected byte entityID = 0;
	//protected Boolean debug = false;
	//protected boolean udp = false;
	
	protected final int MASK_DEBUG 			= 0b00000000000000000000000000000011;
	public static final int DEBUG_FALSE		= 0b00000000000000000000000000000011;
	public static final int DEBUG_TRUE		= 0b00000000000000000000000000000010;
	public static final int DEBUG_DEVELOPER	= 0b00000000000000000000000000000001;
	
	protected final int MASK_PROTOCOL 		= 0b00000000000000000000000000001100;
	public static final int PROTOCOL_TCP 	= 0b00000000000000000000000000000100;
	public static final int PROTOCOL_UDP	= 0b00000000000000000000000000001000;
	
	protected final int MASK_LOCAL			= 0b00000000000000000000000000010000;
	
	
	protected final int MASK_ENTITY			= 0b11111111000000000000000000000000;
	
	protected int val = 0;
	

	public NodeConf(){
		this(0);
	}
	
	public NodeConf(int val){
		this.val = val;
	}
	
	public NodeConf(int val, byte entityID){
		this.val = val;
		setEntityID(entityID);
	}
	
	public void setDebug(Boolean debug){
		if ( debug == null )
			val = (val & ~MASK_DEBUG) | DEBUG_DEVELOPER;
		else if ( debug ){
			val = (val & ~MASK_DEBUG) | DEBUG_TRUE;
		}else{
			val = (val & ~MASK_DEBUG) | DEBUG_FALSE;
		}
	}
	
	public Boolean isDebug(){
		if ( (val & MASK_DEBUG) == DEBUG_DEVELOPER )
			return null;
		if ( (val & MASK_DEBUG) == DEBUG_TRUE )
			return true;
		return false;
	}
	
	public boolean isUDP(){
		return (val & MASK_PROTOCOL) == PROTOCOL_UDP;
	}
	
	public void setUDP(){
		val = (val & ~MASK_PROTOCOL) | PROTOCOL_UDP;
	}

	public boolean isTCP(){
		if ( (val & MASK_PROTOCOL) == 0  )
			return true;
		return (val & MASK_PROTOCOL) == PROTOCOL_TCP;
	}
	
	public void setTCP(){
		val = (val & ~MASK_PROTOCOL) | PROTOCOL_TCP;
	}
	
	public byte getEntityID(){
		return (byte)((val & MASK_ENTITY) >> 24);
	}
	
	public void setEntityID(byte id){
		val = (val & ~MASK_ENTITY) | (id << 24);
	}
	
	
	public void setLocal(boolean local){
		if ( local ){
			val = (val & ~MASK_LOCAL);
		}else {
			val = (val & ~MASK_LOCAL) | MASK_LOCAL; //val | MASK_LOCAL
		}
	}
	
	public boolean isLocal(){
		if ( (val & MASK_LOCAL) == 0 )
			return true;
		return false;
	}
	
	
	public int toInt(){
		return val;
	}
	
	protected void setDebug(int v){
		if ( (v & MASK_DEBUG) == DEBUG_DEVELOPER ){
			val = (val & ~MASK_DEBUG) | DEBUG_DEVELOPER;
			
		}else if ( (v & MASK_DEBUG) == DEBUG_TRUE ){
			val = (val & ~MASK_DEBUG) | DEBUG_TRUE;
			
		}else if ( (v & MASK_DEBUG) == DEBUG_FALSE ){
			val = (val & ~MASK_DEBUG) | DEBUG_FALSE;
		}
	}
	
	protected void setProtocol(int v){
		if ( (v & MASK_PROTOCOL) == PROTOCOL_TCP  ){
			val = (val & ~MASK_PROTOCOL) | PROTOCOL_TCP;
		} else if ( (v & MASK_PROTOCOL) == PROTOCOL_UDP  ){
			val = (val & ~MASK_PROTOCOL) | PROTOCOL_UDP;
		}
	}
	
	
	public void combine(int v){
		this.setDebug(v);
		this.setProtocol(v);
	}
}