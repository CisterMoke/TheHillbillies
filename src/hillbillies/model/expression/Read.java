package hillbillies.model.expression;

import hillbillies.model.statement.WrapStatement;

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
	
	public void setStatement(WrapStatement newstat){
		this.statement=newstat;
	}
	
	public WrapStatement getStatement(){
		return this.statement;
	}
	
	private WrapStatement statement;

	private String name;
}
