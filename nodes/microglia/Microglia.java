package cFramework.nodes.microglia;

public class Microglia {

	private float wellness = 1;

	public void setWellness(float w) {
		this.wellness = w;
		
	}
	
	public float getWellness() {
		return this.wellness;
	}
	
	
	public void limit() {
		if ( this.wellness > 1 )
			this.wellness = 1;
		
	}
	
	public float receive( byte[] data ) {
		if ( data == null ) {
			this.wellness -= 0.05;
		}
		
		if ( this.wellness != 1 ) {
			this.wellness+=0.05;
			this.limit();
		}
		
		
		return this.wellness;
	}
	
	
	public float send() {
		
		if ( this.wellness != 1 ) {
			this.wellness+=0.5;
			this.limit();
		}
		return this.wellness;
	}
	
}
