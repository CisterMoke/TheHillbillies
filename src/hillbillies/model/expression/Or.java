package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.Task;
import hillbillies.model.statement.WrapStatement;

public class Or extends BooleanExpression implements IComposedExpression<Expression<?>>{
	
	public Or(Expression<?> arg1, Expression<?> arg2){
		if(!(arg1 instanceof Read)){
			BooleanExpression left = (BooleanExpression) arg1;
			this.subExpressions.add(left);
		}
		else subExpressions.add(arg1);
		if(!(arg2 instanceof Read)){
			BooleanExpression right = (BooleanExpression) arg2;
			this.subExpressions.add(right);
		}
		else subExpressions.add(arg2);
	}

	@Override
	public Boolean getValue() {
		return (boolean) this.subExpressions.get(0).getValue() || (boolean) this.subExpressions.get(0).getValue();
	}

	@Override
	public ArrayList<Expression<?>> getSubExpressions() {
		return new ArrayList<Expression<?>>(subExpressions);
	}
	
	private ArrayList<Expression<?>> subExpressions = new ArrayList<Expression<?>>();

	@Override
	public void setTask(Task task) {
		this.assignedTask = task;
		for(Expression<?> e : subExpressions)
			e.setTask(task);
	}

	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement=newstat;
		for (Expression<?> expr : subExpressions){
			expr.setWrapStatement(newstat);
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
