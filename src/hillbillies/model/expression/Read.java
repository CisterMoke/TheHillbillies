package hillbillies.model.expression;

import hillbillies.model.statement.SuperStatement;

public class Read extends BasicExpression<Expression<?>>{

	public Read(String key){
		this.setName(key);
	}

	@Override
	public Expression<?> getValue() {
		return (Expression<?>) statement.readVariable(this.getName()).getValue();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setStatement(SuperStatement newstat){
		this.statement=newstat;
	}
	
	public SuperStatement getStatement(){
		return this.statement;
	}
	
	private SuperStatement statement;

	private String name;
}
