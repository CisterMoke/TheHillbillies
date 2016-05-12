package hillbillies.model;

public class Task {
	public Task() {
	}
	
	private Unit unit = null;
	
	private Scheduler scheduler;
	
	private Vector selectedPosition;
	
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
	
	public Vector getSelected(){
		return this.selectedPosition;
	}
	
	public void setSelected(Vector pos){
		this.selectedPosition = pos;
	}
	

}
