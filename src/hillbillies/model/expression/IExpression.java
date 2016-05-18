package hillbillies.model.expression;

import hillbillies.model.Task;
import hillbillies.model.Unit;
import hillbillies.model.statement.WrapStatement;

public interface IExpression<O> {

	public Task getTask();
	
	public Unit getUnit();
	
	public WrapStatement getWrapStatement();
}
