package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.Task;
import hillbillies.model.statement.WrapStatement;

public abstract class ComposedExpression<O,I> extends Expression<O>{
	
	protected ArrayList<Expression<I>> subExpressions = new ArrayList<Expression<I>>();
	
	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement=newstat;
		if (this.hasSubExpression()){
			for (Expression<I> expr : subExpressions){
				expr.setWrapStatement(newstat);
			}
		}
	}
	
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
	
	@Override
	public boolean hasNullExpressions(){
		for (Expression<?> e : subExpressions){
			if(e.hasNullExpressions()){
				return true;
			}
		}
		return this.getValue()==null;
			
	}
}
