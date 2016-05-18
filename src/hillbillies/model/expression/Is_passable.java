package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.Task;
import hillbillies.model.statement.WrapStatement;

public class Is_passable extends BooleanExpression implements IComposedExpression<PositionExpression>{
	
	public Is_passable(PositionExpression e) {
		this.subExpressions.add(e);
	}

	@Override
	public Boolean getValue() {
		return !this.getUnit().getWorld().getBlockAtPos(this.subExpressions.get(0).getValue()).isSolid();
	}
	
	@Override
	public ArrayList<PositionExpression> getSubExpressions() {
		return new ArrayList<PositionExpression>(subExpressions);
	}
	
	private ArrayList<PositionExpression> subExpressions = new ArrayList<PositionExpression>();

	@Override
	public void setTask(Task task) {
		this.assignedTask = task;
		for(PositionExpression e : subExpressions)
			e.setTask(task);
	}

	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement=newstat;
		for (PositionExpression expr : subExpressions){
			expr.setWrapStatement(newstat);
		}
	}

	@Override
	public boolean hasNullExpressions(){
		for (PositionExpression e : subExpressions){
			if(e.hasNullExpressions()){
				return true;
			}
		}
		return this.getValue()==null;
	}

}
