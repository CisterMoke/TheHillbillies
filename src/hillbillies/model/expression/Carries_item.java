package hillbillies.model.expression;

import hillbillies.model.Unit;

public class Carries_item extends ComposedExpression<Boolean, Unit>{
	public Carries_item(Expression<Unit> e) {
		this.subExpressions.add(e);
	}

	@Override
	public Boolean getValue() {
		return this.subExpressions.get(0).getValue().isCarrying();
	}

}
