package kmiddle2.communications.messages.base;

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

import java.nio.ByteBuffer;

public class OperationCode implements OperationCodeConstants {
	
	public static String getOperationCode(short index){
		return getRealName(index);
	}
	
	public static short getOperationCode(byte[] msg){
		ByteBuffer buffer = ByteBuffer.allocate(msg.length);
		buffer.put(msg);
		return buffer.getShort(0);
	}
	
	public static String getRealName(short opCod){
		switch(opCod){
		
			case ACK:
				return "ACK";
			case CREATE:
				return "CREATE";
			case CREATE_AREA_REQUEST:
				return "CREATE_AREA_REQUEST";
			case CREATE_AREA_HELPER_REQUEST:
				return "CREATE_AREA_HELPER_REQUEST";
			case DATA:
				return "DATA";
			case DEAD: 
				return "DEAD";
			case DELETE:
				return "DELETE";
			case FIND_NODE:
				return "FIND_NODE";
			case HANDSHAKE:
				return "HANDSHAKE";
			case HELLO:
				return "HELLO";
			case IGNITE_ENTITY_LIST:
				return "IGNITE_ENTITY_LIST";
			case NEW_NODE:
				return "NEW_NODE";
			case NOFIND_NODE:
				return "NOFIND_NODE";
			case SEARCH_MULTICAST:
				return "SEARCH_MULTICAST";
			case SEARCH_NODE_REQUEST:
				return "SEARCH_NODE";
			case SEND_ME:
				return "SEND_ME";
			case SECOND_DATA:
				return "SECOND_DATA";
			case SINGIN_AREA_NOTIFICATION:
				return "SINGIN_AREA";
			case SINGIN_SIBLING:
				return "SINGIN_SIBLING";
			case SINGIN_ACTIVITY:
				return "SINGIN_CHILD";		
			case UPDATE:
				return "UPDATE";
			default:
				return "N/D";
		
		}
	}
	
	/*public static String toString(short operationCode) {
        String original = String.valueOf(operationCode);
        String last = "";
        for (int i = OPCOD_LENGTH -original.length(); i>0; i--) {
            last+="0";
        }
        return last+original;
    }
	*/
}