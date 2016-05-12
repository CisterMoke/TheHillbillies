package hillbillies.model.expression;

import hillbillies.model.Task;
import hillbillies.model.Unit;
public abstract class Expression<O>{
	
	private Task assignedTask;
	
	public Task getTask(){
		return this.assignedTask;
	}
	
	public void setTask(Task task){
		this.assignedTask = task;
	}
	
	public Unit getUnit(){
		return this.assignedTask.getUnit();
	}
	
	public abstract O getValue();
	
	public abstract boolean hasSubExpression();

	
}
