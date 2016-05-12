package hillbillies.model.expression;

public class Not extends ComposedExpression<Boolean,Boolean>{
	public Not(Expression<Boolean> e) {
		this.subExpressions.add(e);
	}

	@Override
	public Boolean getValue() {
		return !this.subExpressions.get(0).getValue();
	}

}
