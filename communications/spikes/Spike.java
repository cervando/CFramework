package cFramework.communications.spikes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UnknownFormatConversionException;



/**
 * Kuayolotl Middleware System (Si pasa la prueba)
 * @author Ory Medina <omedina@gdl.cinvestav.mx>
 *
 * Generic class that encapsulates the spikes
 * it contains the four fields defined by gtorres:
 * modality, location, intensity and timing of the stimulus
 * 
 * Modality: it represents the type of the stimulus being coded
 * 	e.g. Vision, Touch, Hearing, Memory, etc.
 * 	Modalities are defined in library.utils.fields.modalitiesList.kua
 * 
 * Location: it represents the location of the stimulus, this
 * 	location can be seen as a spatial location (in touch and vision), 
 * 	a spectrum location (a certain frequency in hearing), or 
 * 	a memory class (the class at which this stimulus belongs).
 *  L represents the data type that will be used in the location.
 *  
 * Intensity: it represents the strength of the stimulus, in other
 * 	words, it represents how strong is the stimulus at the previously
 * 	defined location.
 * 	I represents the data type that will be used for the intensity
 * 
 * Timing: it represents the time that the stimulus intensity is present.
 * 	T is the data type that will be used for the timing
 * 
 * @param <L, I, T>
 * L: data type for the location
 * I: data type for the intensity
 * T: data type for the timing
 * Note that all the type parameters must be serializable
 */

public class Spike<L extends Serializable, I extends Serializable, T extends Serializable> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int modality;
	private L location;
	private I intensity;
	private T timing;

	
	/**Constructor**/
	
	/*
	 * @param 
	 * @return
	 * Requires a modality, the rest of the parameters are set to null.
	 */
	public Spike(int modality){
		this.modality = modality;
		this.location = null;
		this.intensity = null;
		this.timing = null;
	}
	
	public Spike(int modality, L location, I intensity, T timing){
		this.modality = modality;
		this.location = location;
		this.intensity = intensity;
		this.timing = timing;
	}
	
	public Spike(byte[] spike) throws Exception{
		short count = 0;
		byte[] tmp = new byte[2];
		
		//Get back the data lengths
		System.arraycopy(spike, count, tmp, 0, 2);
		short mShort = shortFromByte(tmp);
		count += 2;
		System.arraycopy(spike, count, tmp, 0, 2);
		short lShort = shortFromByte(tmp);
		count += 2;
		System.arraycopy(spike, count, tmp, 0, 2);
		short iShort = shortFromByte(tmp);
		count += 2;
		System.arraycopy(spike, count, tmp, 0, 2);
		short tShort = shortFromByte(tmp);
		count += 2;
		
		//Recover the data
		//Modality
		tmp = new byte[mShort];
		System.arraycopy(spike, count, tmp, 0, mShort);
		this.modality = modalityFromByte(tmp);
		count += mShort;
			
		//Location
		tmp = new byte[lShort];
		System.arraycopy(spike, count, tmp, 0, lShort);
        this.location = objectFromByte(tmp);
		count += lShort;
		
		//Intensity
		tmp = new byte[iShort];
		System.arraycopy(spike, count, tmp, 0, iShort);
	    this.intensity = objectFromByte(tmp);
	    count += iShort;
			
	    //Timing
		tmp = new byte[tShort];
		System.arraycopy(spike, count, tmp, 0, tShort);
	    this.timing = objectFromByte(tmp);
		count += tShort;
	}
	
	/**Specific/static methods**/
	
	public byte[] getByteArray() throws IOException{
		/*
		 * First 8 bytes contain the length of the 4 fields (in bytes)
		 * The following bytes contain the data
		 */
		
		//Modality
		byte[] mByte = modalityToByte(this.modality);
		short mShort = (short)mByte.length;
		
		//Location
		byte[] lByte = objectToByte(this.location);
		short lShort = (short)lByte.length;
	
		//Intensity
		byte[] iByte = objectToByte(this.intensity);
		short iShort = (short)iByte.length;
	
		//Timing
		byte[] tByte = objectToByte(this.timing);
		short tShort = (short)tByte.length;
    
		//Create full byte array	
		byte[] full = new byte[8 + mShort + lShort + iShort + tShort];
	
		//Fill full byte array
		//Start with the sizes
		int i = 0;
		System.arraycopy(shortToByte(mShort), 0, full, i, 2);
		i += 2;
		System.arraycopy(shortToByte(lShort), 0, full, i, 2);
		i += 2;
		System.arraycopy(shortToByte(iShort), 0, full, i, 2);
		i += 2;
		System.arraycopy(shortToByte(tShort), 0, full, i, 2);
		i += 2;
		
		//Now the data
		System.arraycopy(mByte, 0, full, i, mShort);
		i += mShort;
		System.arraycopy(lByte, 0, full, i, lShort);
		i += lShort;
		System.arraycopy(iByte, 0, full, i, iShort);
		i += iShort;
		System.arraycopy(tByte, 0, full, i, tShort);
		i += tShort;
		
		return full;
	}
	
	public static int getModality(byte [] spike){
		byte[] tmp = new byte[4];
		System.arraycopy(spike, 8, tmp, 0, 4);
		ByteBuffer buffer = ByteBuffer.wrap(tmp);
		return buffer.getInt();
	}
	
	public static Object getLocation(byte [] spike) throws IOException, ClassNotFoundException{
		byte [] tmp = new byte[2];
		System.arraycopy(spike, 2, tmp, 0, 2);
		ByteBuffer buffer = ByteBuffer.wrap(tmp);
		short length =  buffer.getShort();
		
		tmp = new byte[length];
		System.arraycopy(spike, 12, tmp, 0, length);
		ByteArrayInputStream b = new ByteArrayInputStream(tmp);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
	}
	
	/**Set-Get methods**/
	
	public void setModality(int modality){
		this.modality = modality;
	}
	
	public void setLocation(L location){
		this.location = location;
	}
	
	public void setIntensity(I intensity){
		this.intensity = intensity;
	}
	
	public void setTiming(T timing){
		this.timing = timing;
	}
	
	public int getModality(){
		return this.modality;
	}
	
	/*
	public String getModalityName(){
		return ModalityStrings.getModality(this.modality);
	}*/
	
	
	public L getLocation(){
		return this.location;
	}
	
	public I getIntensity(){
		return this.intensity;
	}
	
	public T getTiming(){
		return this.timing;
	}
	
	/**Standard methods**/
	
	/*
	 * @param null
	 * @return Spike
	 * Returns an exact copy of this Spike
	 */
	public Spike<L, I, T> clone(){
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public boolean equals(Spike<L, I, T> spike){
		try{
			if(spike.getModality() == this.modality && spike.getLocation().equals(this.location) && spike.getIntensity().equals(this.intensity) && spike.getTiming().equals(this.timing))
				return true;
			else
				return false;
		}
		catch(Exception ex){
			throw new UnknownFormatConversionException("Unsoported data types");
		}
	}
	
	public int hashCode(){
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	
	/*
	public String toString(){
		try{
			return "Modality: " + ModalityStrings.getModality(modality) + 
				   "\nLocation: " + location.toString() + 
				   "\nIntensity: " + intensity.toString() + 
				   "\nTiming: " + timing.toString();
		}
		catch(Exception ex){
			throw new UnknownFormatConversionException("Unsoported data types");
		}
	}
	*/
	
	private byte[] modalityToByte(int modality) throws IOException{
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(modality);
		return buffer.array();
	}
	
	private <M> byte[] objectToByte(M obj) throws IOException{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}
	
	private byte[] shortToByte(short n) throws IOException{
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(n);
		return buffer.array();
	}
	
	private int modalityFromByte(byte[] bytes){
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getInt();
	}
	
	@SuppressWarnings("unchecked")
	private <M> M objectFromByte(byte[] bytes) throws IOException, ClassNotFoundException{
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (M)o.readObject();
	}
	
	private short shortFromByte(byte[] bytes){
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getShort();
	}
}
