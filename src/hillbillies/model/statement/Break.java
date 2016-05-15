package hillbillies.model.statement;

public class Break extends BasicStatement{
	
	public Break(){
	}

	@Override
	public void executeSpecific() {
		if (this.getWrapStatement()!=null)
			this.getWrapStatement().stopLoop();
		System.out.println("Place break inside a loop");
	}
	
	@Override
	public boolean isWellFormed(){
		if (this.getWrapStatement()!=null)
			return this.getWrapStatement().stopLoop();
		System.out.println("Place break inside a loop");
		return false;
	}

}
