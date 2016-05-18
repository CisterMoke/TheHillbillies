package hillbillies.model.expression;

import hillbillies.model.Task;
import hillbillies.model.Vector;
import hillbillies.model.statement.WrapStatement;

public class BoulderLiteral extends PositionExpression implements IBasicExpression{

	public BoulderLiteral(){
		
	}

	@Override
	public Vector getValue() {
		if (!(this.getUnit().getClosestBoulder()==null))
			return this.getUnit().getClosestBoulder().getPosition().floor();
		else return null;
	}

	@Override
	public void setTask(Task task) {
		this.assignedTask = task;
		
	}

	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement = newstat;
		
	}

	@Override
	public boolean hasNullExpressions() {
		return getValue() == null;
	}
	
	
}
