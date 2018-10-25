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
package kmiddle2.util;

import kmiddle2.communications.MessageMetadata;

public class BinaryHelper {

	public static short byteToShort(byte[] bin, int startIndex){
		short r = (short)((bin[startIndex] << 8) & 0xFFFF);
		r += (short)(bin[startIndex+1] & 0xFF);
		return r;
	}
		
	public static int byteToUnsignedShort(byte[] bin, int startIndex){
		int r = (int)((bin[startIndex] << 8) & 0xFFFF);
		r += (int)(bin[startIndex+1] & 0xFF);
		return r;
	}
	
	
	
	public static final int MessageMetadataBytesLengh = 4;
	/**
	 * Transforms a sequence of bytes into a Time unit
	 * @param bin
	 * @param startIndex
	 * @return
	 */
	public static MessageMetadata byteToMessageMetaData(byte[] bin, int startIndex){
		int r = 0;
		r += (int)((bin[startIndex    ] << 24) & 0xFFFFFFFF);
		r += (int)((bin[startIndex + 1] << 16) & 0xFFFFFF);
		r += (int)((bin[startIndex + 2] << 8 ) & 0xFFFF);
		r += (int)(bin[startIndex  + 3] 	   & 0xFF);
		return new MessageMetadata(r);
	}
	/**
	 * Transforms a Time unit into a sequence of bytes
	 * @param bin
	 * @param startIndex
	 * @return
	 */
	public static byte[] MessageMetadataToByte( MessageMetadata meta ){
		int i = meta.time;
		byte r[] = new byte[4];
		r[0] = (byte) ((i >> 24) & 0xFF);
		r[1] = (byte) ((i >> 16) & 0xFF);
		r[2] = (byte) ((i >> 8)  & 0xFF);
		r[3] = (byte) (   i  	 & 0xFF);
		return r;
	}
	
	
	
	public static int byteToInt(byte[] bin, int startIndex){
		int r = 0;
		r += (int)((bin[startIndex    ] << 24) & 0xFFFFFFFF);
		r += (int)((bin[startIndex + 1] << 16) & 0xFFFFFF);
		r += (int)((bin[startIndex + 2] << 8 ) & 0xFFFF);
		r += (int)(bin[startIndex  + 3] 	   & 0xFF);
		return r;
	}
	
	public static String byteToIP(byte[] b, int startIndex){
		String ip = "";
		ip += (Short.toString( (short)( b[startIndex] & 0xFF )) + ".");
		ip += (Short.toString( (short)( b[startIndex+1] & 0xFF )) + ".");
		ip += (Short.toString( (short)( b[startIndex+2] & 0xFF )) + ".");
		ip += (Short.toString( (short)( b[startIndex+3] & 0xFF )));
		return ip;
	}
	
	public static String byteToString ( byte[] bin, int startIndex, int lenght ){
		String ret = "";
		for (int i = 0; i < lenght; i++){
			ret += (char) bin[ startIndex + i ];
		}
		return ret;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] stringToByte( String s ){
		return mergeByteArrays(new byte[]{(byte)s.length()}, s.getBytes());
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByte( short s ){
		return new byte[]{
			(byte)(s >> 8 & 0xFF),
			(byte) s
		};
	}
	
	public static byte[] intToByte( int i ){
		byte r[] = new byte[4];
		r[0] = (byte) ((i >> 24) & 0xFF);
		r[1] = (byte) ((i >> 16) & 0xFF);
		r[2] = (byte) ((i >> 8)  & 0xFF);
		r[3] = (byte) (   i  	 & 0xFF);
		return r;
	}
	
	
	
	
	/**
	 * Concatena dos arreglos de bytes y retorna el resultado
	 * @param a Arreglo de bytes 1
	 * @param b Arreglo de bytes 2
	 * @return
	 */
	public static byte[] mergeByteArrays(byte[] a,byte[] b){
		return mergeByteArrays(a, b, new byte[0], new byte[0], new byte[0], new byte[0], new byte[0]);
	}
	
	public static byte[] mergeByteArrays(byte[] a,byte[] b, byte[] c){
		return mergeByteArrays(a, b, c, new byte[0], new byte[0], new byte[0], new byte[0]);
	}
	
	public static byte[] mergeByteArrays(byte[] a,byte[] b, byte[] c, byte[] d){
		return mergeByteArrays(a, b, c, d, new byte[0], new byte[0], new byte[0]);
	}
	
	public static byte[] mergeByteArrays(byte[] a,byte[] b, byte[] c, byte[] d, byte[] e){
		return mergeByteArrays(a, b, c, d, e, new byte[0], new byte[0]);
	}
	
	public static byte[] mergeByteArrays(byte[] a,byte[] b, byte[] c, byte[] d, byte[] e, byte[] f){
		return mergeByteArrays(a, b, c, d, e, f, new byte[0]);
	}
	
	public static byte[] mergeByteArrays(byte[] a,byte[] b, byte[] c, byte[] d, byte[] e, byte[] f, byte[] g){
		byte[] r = new byte[a.length+b.length+c.length+d.length+e.length+f.length+g.length];
		
		int p = 0;
		System.arraycopy(a, 0, r, p, a.length);
		if ( (p+= a.length) >= r.length ) return r;
	
		System.arraycopy(b, 0, r, p, b.length);
		if ( (p+= b.length) >= r.length ) return r;
		
		System.arraycopy(c, 0, r, p, c.length);
		if ( (p+= c.length) >= r.length ) return r;
		
		System.arraycopy(d, 0, r, p, d.length);
		if ( (p+= d.length) >= r.length ) return r;
		
		System.arraycopy(e, 0, r, p, e.length);
		if ( (p+= e.length) >= r.length ) return r;
		
		System.arraycopy(f, 0, r, p, f.length);
		if ( (p+= f.length) >= r.length ) return r;
		
		System.arraycopy(g, 0, r, p, g.length);	
		
		return r;
	}
	
	/**
	 * Regresa una parte del arreglo de bytes, delimitada por los parametrs
	 * Encaso de que la posicion inicial mas la longitud sea mayor a la longitud del arreglo, arrojara un error
	 * @param bin 		Arreglo original
	 * @param startIndex
	 * @param length
	 * @return
	 */
	public static byte[] subByteArray(byte[] bin, int startIndex, int length){
		if ( bin.length < startIndex + length ){
			throw new IndexOutOfBoundsException("Error al conseguir subsArray");
		}
		byte[] c = new byte[length];
		System.arraycopy(bin, startIndex, c, 0, length);
		return c;
	}

}
