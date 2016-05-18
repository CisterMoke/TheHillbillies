package hillbillies.model.expression;

import hillbillies.model.Task;
import hillbillies.model.Vector;
import hillbillies.model.statement.WrapStatement;

public class WorkshopLiteral extends PositionExpression implements IBasicExpression{
	
	public WorkshopLiteral() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector getValue() {
		if (!(this.getTask().getUnit().getClosestWorkshop()==null))
			return this.getTask().getUnit().getClosestWorkshop().getLocation();
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
