package hillbillies.model.statement;

import be.kuleuven.cs.som.annotate.Basic;
import hillbillies.model.expression.Expression;

public class Assignment extends BasicStatement{
	
	public Assignment(String n, Expression<?> v){
		addExpression(v);
		this.setName(n);
	}
	
	@Override
	public void executeSpecific() {
		this.getWrapStatement().addVariable(this.getName(), this.getValue());
	}
	
	@Basic
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return Expressions.get(0).getValue();
	}

	private String name;
}
