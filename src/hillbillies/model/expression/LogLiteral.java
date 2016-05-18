package hillbillies.model.expression;

import hillbillies.model.Task;
import hillbillies.model.Vector;
import hillbillies.model.statement.WrapStatement;

public class LogLiteral extends PositionExpression implements IBasicExpression{
	
	public LogLiteral() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector getValue() {
		if (!(this.getUnit().getClosestLog()==null))
			return this.getUnit().getClosestLog().getPosition().floor();
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
