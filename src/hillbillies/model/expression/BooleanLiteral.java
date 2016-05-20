package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.Basic;
import hillbillies.model.Task;
import hillbillies.model.statement.WrapStatement;

public class BooleanLiteral extends BooleanExpression implements IBasicExpression{
	
	public BooleanLiteral(boolean value){
		this.value = value;
	}
	
	private final boolean value;

	@Override @Basic
	public Boolean getValue() {
		return this.value;
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
