package hillbillies.model.statement;

import hillbillies.model.expression.Expression;

public class Assignment extends BasicStatement{
	
	public Assignment(String n, Expression<?> v){
		this.setName(n);
		this.setValue(v);
	}
	
	@Override
	public void executeSpecific() {
//		if (super.getCompleted())
//			return;
//		super.task.countDown();
		this.getWrapStatement().addVariable(this.getName(), this.getValue());
//		super.setCompleted(true);
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
