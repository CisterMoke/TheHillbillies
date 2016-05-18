package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.Task;
import hillbillies.model.statement.WrapStatement;

public class And extends BooleanExpression implements IComposedExpression<BooleanExpression>{
	
	public And(BooleanExpression left, BooleanExpression right){
		this.subExpressions.add(left);
		this.subExpressions.add(right);
	}

	@Override
	public Boolean getValue() {
		return this.subExpressions.get(0).getValue() && this.subExpressions.get(0).getValue();
	}

	@Override
	public ArrayList<BooleanExpression> getSubExpressions() {
		return new ArrayList<BooleanExpression>(subExpressions);
	}
	
	private ArrayList<BooleanExpression> subExpressions = new ArrayList<BooleanExpression>();

	@Override
	public void setTask(Task task) {
		this.assignedTask = task;
		for(BooleanExpression e : subExpressions)
			e.setTask(task);
	}

	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement=newstat;
		for (BooleanExpression expr : subExpressions){
			expr.setWrapStatement(newstat);
		}
	}

	@Override
	public boolean hasNullExpressions(){
		for (BooleanExpression e : subExpressions){
			if(e.hasNullExpressions()){
				return true;
			}
		}
		return this.getValue()==null;
	}
}
