
public class State {
	byte neoX,neoY;
	short movedHostages;
	short currentlyCarriedHostages;
	short hostagesToAgents;
	short killedTransHostages;
	public State(byte neoX, byte neoY, short movedHostages, short currentlyCarriedHostages, short hostagesToAgents,
			short killedTransHostages, byte[] hostagesHealth, byte neoHealth) {
		super();
		this.neoX = neoX;
		this.neoY = neoY;
		this.movedHostages = movedHostages;
		this.currentlyCarriedHostages = currentlyCarriedHostages;
		this.hostagesToAgents = hostagesToAgents;
		this.killedTransHostages = killedTransHostages;
		this.hostagesHealth = hostagesHealth;
		this.neoHealth = neoHealth;
	}
	byte[] hostagesHealth;
	byte neoHealth;
	
}
