package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.Basic;
import hillbillies.model.Task;
import hillbillies.model.Unit;
import hillbillies.model.statement.WrapStatement;
public abstract class Expression<O> implements IExpression<O>{
	
	protected Task assignedTask;
	
	@Override @Basic
	public Task getTask(){
		return this.assignedTask;
	}
	
	public abstract void setTask(Task task);
	
	@Override
	public Unit getUnit(){
		return this.assignedTask.getUnit();
	}
	
	public abstract void setWrapStatement(WrapStatement newstat);
	
	@Override @Basic
	public WrapStatement getWrapStatement(){
		return this.statement;
	}
	
	public abstract O getValue();
	
	protected WrapStatement statement;
	
	public abstract boolean hasNullExpressions();

	
}
