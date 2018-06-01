/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmiddle2.communications.messages.base;

/**
 * 
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
public interface OperationCodeConstants {

	final short IGNITE_ENTITY_LIST = 30;

	final short ACK = 1;

	final short AVAILABLE = 2;

	final short CREATE = 17;

	final short DATA = 18;

	final short DEAD = 19;

	final short DELETE = 20;

	final short CREATE_AREA_REQUEST = 21;
	
	final short CREATE_AREA_HELPER_REQUEST = 22;
	
	final short UPDATE = 23;

	final short HANDSHAKE = 49;

	final short HELLO = 50;

	final short FIND_NODE = 609;

	final short IM_DEAD = 1041;

	final short NEW_NODE = 1633;

	final short NOFIND_NODE = 1634;

	final short OK_AVAILABLE = 1793;

	final short SECOND_DATA = 2323;

	final short SEARCH_MULTICAST = 2401;

	final short SEARCH_NODE_REQUEST = 2402;

	final short SEND_ME = 2404;

	final short SINGIN_AREA_NOTIFICATION = 2405;

	final short SINGIN_SIBLING = 2406;
	
	final short SINGIN_ACTIVITY = 2407;
	
	final short FREE_NODE = 2408;

}
