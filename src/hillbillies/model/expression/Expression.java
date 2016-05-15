package hillbillies.model.expression;

import hillbillies.model.Task;
import hillbillies.model.Unit;
import hillbillies.model.statement.WrapStatement;
public abstract class Expression<O>{
	
	protected Task assignedTask;
	
	public Task getTask(){
		return this.assignedTask;
	}
	
	public void setTask(Task task){
		this.assignedTask = task;
	}
	
	public Unit getUnit(){
		return this.assignedTask.getUnit();
	}
	
	public void setStatement(WrapStatement newstat){
		this.statement=newstat;
	}
	
	public WrapStatement getStatement(){
		return this.statement;
	}
	
	protected WrapStatement statement;
	
	public abstract O getValue();
	
	public abstract boolean hasSubExpression();

	
}
