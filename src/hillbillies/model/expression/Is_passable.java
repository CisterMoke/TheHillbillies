package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.Task;
import hillbillies.model.Vector;
import hillbillies.model.statement.WrapStatement;

public class Is_passable extends BooleanExpression implements IComposedExpression<Expression<?>>{
	
	public Is_passable(Expression<?> e) {
		if(!(e instanceof Read)){
			PositionExpression exp = (PositionExpression) e;
			this.subExpressions.add(exp);
		}
		else subExpressions.add(e);
	}

	@Override
	public Boolean getValue() {
		Vector v = (Vector) this.subExpressions.get(0).getValue();
		return !this.getUnit().getWorld().getBlockAtPos(v).isSolid();
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
