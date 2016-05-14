package hillbillies.model.statement;

import hillbillies.model.expression.Expression;

public class Assignment extends Statement{
	
	public Assignment(String n, Expression<?> v){
		v.setTask(super.getTask());
		this.setName(n);
		this.setValue(v);
	}
	
	@Override
	public void execute() {
		if (super.getCompleted())
			return;
		super.task.countDown();
		this.getSuperStatement().addVariable(this.getName(), this.getValue());
		super.setCompleted(true);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Expression<?> getValue() {
		return value;
	}
	public void setValue(Expression<?> value) {
		this.Expressions.add(value);
		this.value = value;
	}

	private String name;
	
	private Expression<?> value;

}
