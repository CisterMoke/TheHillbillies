package hillbillies.model.expression;

public class Read extends ComposedExpression<Expression<?>, String>{

	public Read(String key){
		this.setName(key);
	}

	@Override
	public Expression<?> getValue() {
		return super.getTask().readVariable(this.getName());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
}
