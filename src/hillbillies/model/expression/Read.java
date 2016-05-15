package hillbillies.model.expression;

import hillbillies.model.statement.WrapStatement;

public class Read extends BasicExpression<Object>{

	public Read(String key){
		this.setName(key);
	}

	@Override
	public Object getValue() {
		if (this.statement!=null)
			if (this.statement.readVariable(this.getName()) !=null)
				return statement.readVariable(this.getName()).getValue();
			else {
				return null;
			}
		else {
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
}
