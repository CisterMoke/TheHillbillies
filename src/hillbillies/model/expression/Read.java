package hillbillies.model.expression;

import hillbillies.model.statement.WrapStatement;

public class Read extends BasicExpression<Object>{

	public Read(String key){
		this.setName(key);
	}

	@Override
	public Object getValue() {
		System.out.println(statement);
		return statement.readVariable(this.getName()).getValue();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
}
