package cFramework.communications.spikes;

import java.util.HashMap;

public interface SpikeMerger {
	//public Spike merge(HashMap<Integer,Spike> spikes);
	public byte[] merge(HashMap<Long,byte[]> spikes);
}