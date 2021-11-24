
public class State {
	byte neoX,neoY;
	short movedHostages;
	short currentlyCarriedHostages;
	short hostagesToAgents;
	short killedTransHostages;
	short pills;

	
	
	// interface/object that have a methods that makes the required operations easy
	long killedNormalAgent0, killedNormalAgent1, killedNormalAgent2;
	int killedNormalAgent3;
	byte[] hostagesHealth;
	byte neoHealth;
	public State(byte neoX, byte neoY, short movedHostages, short currentlyCarriedHostages, short hostagesToAgents,
			short killedTransHostages, long killedNormalAgent0, long killedNormalAgent1, long killedNormalAgent2,
			int killedNormalAgent3, byte[] hostagesHealth, byte neoHealth,short pills) {
		super();
		this.neoX = neoX;
		this.neoY = neoY;
		this.movedHostages = movedHostages;
		this.currentlyCarriedHostages = currentlyCarriedHostages;
		this.hostagesToAgents = hostagesToAgents;
		this.killedTransHostages = killedTransHostages;
		this.killedNormalAgent0 = killedNormalAgent0;
		this.killedNormalAgent1 = killedNormalAgent1;
		this.killedNormalAgent2 = killedNormalAgent2;
		this.killedNormalAgent3 = killedNormalAgent3;
		this.hostagesHealth = hostagesHealth;
		this.neoHealth = neoHealth;
		this.pills = pills;

	}
	
	public static void main(String[] args) {
		short x = 255;
		System.out.println((byte)x);
	}
	
	
}
