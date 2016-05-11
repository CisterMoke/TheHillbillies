package hillbillies.model;

public class Task {
	
	private Unit unit = null;
	
	private Scheduler scheduler;
	
	public boolean isAssigned(){
		return this.getUnit() == null;
	}
	
	public Unit getUnit(){
		return this.unit;
	}
	
	public void setUnit(Unit unit){
		//TODO worldcheck enzo
		this.unit = unit;
	}
	

}
