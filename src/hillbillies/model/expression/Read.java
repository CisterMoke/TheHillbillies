package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.Basic;
import hillbillies.model.Task;
import hillbillies.model.statement.WrapStatement;

public class Read extends Expression<Object> implements IBasicExpression{

	public Read(String key){
		this.setName(key);
	}

	@Override
	public Object getValue() {
		if (this.statement!=null && this.statement.readVariable(this.getName()) !=null)
			return statement.readVariable(this.getName());
		else {
			return null;
		}
	}

	@Basic
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

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
