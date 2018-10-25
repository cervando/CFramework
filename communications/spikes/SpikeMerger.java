package kmiddle2.communications.spikes;

import java.util.HashMap;

public interface SpikeMerger {
	//public Spike merge(HashMap<Integer,Spike> spikes);
	public byte[] merge(HashMap<Integer,byte[]> spikes);
}