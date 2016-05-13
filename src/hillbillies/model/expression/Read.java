package hillbillies.model.expression;

public class Read extends BasicExpression<Expression<?>>{

	public Read(String key){
		this.setName(key);
	}

	@Override
	public Expression<?> getValue() {
		return (Expression<?>) super.getTask().readVariable(this.getName()).getValue();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
}
