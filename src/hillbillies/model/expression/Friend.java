package hillbillies.model.expression;

import hillbillies.model.Task;
import hillbillies.model.Unit;
import hillbillies.model.statement.WrapStatement;

public class Friend extends UnitExpression implements IBasicExpression{
	public Friend() {
	}

	@Override
	public Unit getValue() {
		return this.getUnit().getClosestFriend();
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
