
public class Trying {
	
	static int[] hostagesLocation;
	
	// Return index of hostage at X,Y or -1 if no hostage here
	private static short hostageAt(byte x, byte y, 
			byte[] hosHealth, short movedHos) {
		
		for(byte i=(byte)0;i<hosHealth.length;i++){
			// If init location isn't here no need to check
			if(x != hostagesLocation[2*i] 
					|| y != hostagesLocation[2*i+1])
				continue;
			
			// If hostage that started at x,y was moved there is hostage one here
			if((movedHos & (1<<i)) != 0)
				return Short.MIN_VALUE;
			
			// If hostage is alive
			if(hosHealth[i] < 100){
				return i;
			}
		}
		return -1;
	}
}
