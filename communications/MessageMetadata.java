package cFramework.communications;

/**	Simple class to Encapsule a time Abstraction
 * This class was created because the time absctraccion is not defined, is more mantenible to create a class for those cases
 * 
 * @author Armando Cervantes
 *
 */
public class MessageMetadata {
	
	public MessageMetadata(int time) {
		this.time=time;
	}
	public int time;
}
