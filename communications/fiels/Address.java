package kmiddle2.communications.fiels;

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

public class Address implements NullValueConstants {
	private String ip;
	private int port;
	
	public Address(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public Address(){
		ip = NULL_STRING;
		port = NULL_INT;
	}
	
	/**
	 *Crea un objeto de tipo Address a partir de un arreglo de bytes
	 *Es nesesario que sea un byte por cada numero de la IP y un short para el puerto 
	 * @param b Cadena binaria que contene la Address
	 */
	public Address(byte[] b){
		this(b,0);
	}

	/**
	 * Crea un objeto de tipo Address a partir de un arreglo de bytes
	 * Es nesesario que sea un byte por cada numero de la IP y un short para el puerto, 
	 * @param b Cadena binaria que contene la Address
	 * @param startIndex Casillero de Inicio de la Address
	 */
	public Address(byte[] b, int startIndex){
		ip = "";
		ip += (Short.toString( (short)( b[startIndex] & 0xFF )) + ".");
		ip += (Short.toString( (short)( b[startIndex+1] & 0xFF )) + ".");
		ip += (Short.toString( (short)( b[startIndex+2] & 0xFF )) + ".");
		ip += (Short.toString( (short)( b[startIndex+3] & 0xFF )));
		port = (int) ((b[startIndex+4] << 8) & 0xFFFF);
		port += (int) (b[startIndex+5] & 0xFF);
	}
	
	public void setIp(String i){
		ip = i;
	}
	
	public void setPort(int p){
		port = p;
	}
	
	public String getIp(){
		return ip;
	}
	
	public int getPort(){
		return port;
	}
	
	@Override
	public boolean equals(Object o){
		if(o.getClass()==Address.class){
			Address a = (Address)o;
			return a.getIp().compareTo(ip)==0 && a.getPort()==port;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + (this.ip != null ? this.ip.hashCode() : 0);
		hash = 89 * hash + this.port;
		return hash;
	}
	
	@Override
	public String toString(){
		return "["+getIp()+","+getPort()+"]";
	}
	
	public byte[] toByteArray(){
		byte[] r = new byte[6];
		int start = 0;
		int point = 0;
		for( int i = 0; i < 4; i++){
			point = ip.indexOf(".", point + 1);
			if( point == -1)
				r[i] = Byte.valueOf( toUnsignedByte(ip.substring(start)) );
			else
				r[i] = Byte.valueOf( toUnsignedByte(ip.substring(start, point)) );
			start = point + 1;
		}
		r[4] = (byte) ((port >> 8) & 0xFF);
		r[5] = (byte) (port & 0xFF);
		return r;
		
	}
	
	public boolean isNullIp(){
		return getIp().compareTo(NULL_STRING)==0 ;
	}
	
	public boolean isNullPort(){
		return getPort()==NULL_INT;
	}
	
	private byte toUnsignedByte( String  value){
		return (byte)(((byte)Short.parseShort( value ))  & 0xFF);
	}
	
	public static void main ( String args[] ){
		System.out.println(new Address(new Address("10.0.5.238",60390).toByteArray()).toString());
		 
	}
}