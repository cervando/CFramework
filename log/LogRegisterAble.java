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
package kmiddle2.log;

import kmiddle2.communications.fiels.Address;

public interface LogRegisterAble {
	
	public String header(int name, Address address);
	
	public String send(int name,String more);
	public String send(Address addr, String more);
	public String send_debug(int name, String more);
	
	
	public String receive(int name,String more);
	public String receive(Address addr, String more);
	public String receive_debug(int name, String more);
	
	
	public String saveRequest(int name,String more);
	
	public String error(String more);
	
	public String info(String more);
	public String info(String more, int node);
	
	public String developer(String more);
	public String developer(String more, int node);
}
