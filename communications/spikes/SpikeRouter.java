package cFramework.communications.spikes;

import java.util.Arrays;

public class SpikeRouter {
	
	
	private long[] to_sorted;
	public long ROUTERID = 0;
	
	public long[] from;
	public long[] to;
	public SpikeMerger merger = null;	
	
	
	public SpikeRouter(long[] from, long[] to) {
		this.from = from;
		this.to = to;
		this.to_sorted = to.clone();
		Arrays.sort(to.clone());
	}
	
	
	public SpikeRouter(long[] from, long[] to, SpikeMerger merger) {
		this.from = from;
		this.to = to;
		this.to_sorted = to.clone();
		Arrays.sort(to.clone());
		this.merger = merger;
	}
	
	public boolean isTargetToID( long sendToID) {
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
