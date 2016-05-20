package hillbillies.model.expression;

import hillbillies.model.Task;
import hillbillies.model.Vector;
import hillbillies.model.statement.WrapStatement;

public class Here extends PositionExpression implements IBasicExpression{
	public Here() {
	}

	@Override
	public Vector getValue() {
		return this.getUnit().getPosition().floor();
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
