
public class Trying {
	
	static int[] hostagesLocation;
	
	// Return index of hostage at X,Y or -1 if no hostage here
	private static short hostageToDrop(byte x, byte y, 
			byte[] hosHealth, short carriedHos) {
		
		int bestIdx = -1;
		int bestHealth = 0;
		for(int i = 0; i<hosHealth.length;i++) {
			// Neo is carrying this hostage
			if((carriedHos & (1<<i)) != 0)
			{
				
			}
		}
		return -1;
	}
	
	private static short dropAllHostages(byte x, byte y, 
			byte[] hosHealth, short carriedHos) {
		
		for(int i = 0; i<hosHealth.length;i++) {
			// Neo is carrying this hostage
			if((carriedHos & (1<<i)) != 0)
			{
				carriedHos = 0;
			}
		}
		return -1;
	}
}
