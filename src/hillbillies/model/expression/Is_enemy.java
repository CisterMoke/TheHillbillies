package hillbillies.model.expression;

import hillbillies.model.Unit;

public class Is_enemy extends ComposedExpression<Boolean, Unit>{
	
	public Is_enemy(Expression<Unit> e) {
		this.subExpressions.add(e);
	}

	@Override
	public Boolean getValue() {
		return this.getUnit().getFaction() != this.subExpressions.get(0).getValue().getFaction();
	}
	
	

}
