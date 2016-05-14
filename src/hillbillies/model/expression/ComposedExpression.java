package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.Task;

public abstract class ComposedExpression<O,I> extends Expression<O>{
	
	public ArrayList<Expression<I>> subExpressions = new ArrayList<Expression<I>>();
	
	@Override
	public boolean hasSubExpression(){
		return true;
	}
	
	@Override
	public void setTask(Task task){
		this.assignedTask = task;
		if (this.hasSubExpression()){
			for (Expression<I> expr : subExpressions){
				expr.setTask(task);
			}
		}
	}
}
