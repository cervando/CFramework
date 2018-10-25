package kmiddle2.communications.spikes;

import java.util.Arrays;

public class SpikeRouter {
	
	
	private int [] to_sorted;
	public int ROUTERID = 0;
	
	public int[] from;
	public int[] to;
	public SpikeMerger merger = null;	
	
	
	public SpikeRouter(int[] from, int[] to) {
		this.from = from;
		this.to = to;
		this.to_sorted = to.clone();
		Arrays.sort(to.clone());
	}
	
	
	public SpikeRouter(int[] from, int[] to, SpikeMerger merger) {
		this.from = from;
		this.to = to;
		this.to_sorted = to.clone();
		Arrays.sort(to.clone());
		this.merger = merger;
	}
	
	public boolean isTargetToID( int sendToID) {
		return Arrays.binarySearch(to_sorted, sendToID) >= 0;
	}
	/*
	public static void main(String [] b) {
		SpikeRouter a = new SpikeRouter(
			new int[] {1,2}, 
			new int[] {3},
			new SpikeMerger() {
				public Spike merge(HashMap<Integer,Spike> spikes) {
					return null;
				}
			}
		);
		a.merger.merge(null);
	}
	*/
}
