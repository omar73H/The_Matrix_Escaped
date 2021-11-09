
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
	
	public static void main(String[] args) {
		int[] a = new int[]{1,2,3,4,5,6,7,8,9,10};
		
		for(int i = 0;i<a.length/2;i+=2)
			System.out.println(i +" " +(i+1));
	}
}
