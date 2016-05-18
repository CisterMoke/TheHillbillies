package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.Task;
import hillbillies.model.Vector;
import hillbillies.model.statement.WrapStatement;

public class Position_of extends PositionExpression implements IComposedExpression<UnitExpression>{
	
	public Position_of(UnitExpression e) {
		this.subExpressions.add(e);
	}

	@Override
	public Vector getValue() {
		return this.subExpressions.get(0).getValue().getPosition().floor();
	}
	
	@Override
	public ArrayList<UnitExpression> getSubExpressions() {
		return new ArrayList<UnitExpression>(subExpressions);
	}
	
	private ArrayList<UnitExpression> subExpressions = new ArrayList<UnitExpression>();

	@Override
	public void setTask(Task task) {
		this.assignedTask = task;
		for(UnitExpression e : subExpressions)
			e.setTask(task);
	}

	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement=newstat;
		for (UnitExpression expr : subExpressions){
			expr.setWrapStatement(newstat);
		}
	}

	@Override
	public boolean hasNullExpressions(){
		for (UnitExpression e : subExpressions){
			if(e.hasNullExpressions()){
				return true;
			}
		}
		return this.getValue()==null;
	}

}
