package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.model.Vector;

public class Position_of extends ComposedExpression<Vector,Unit>{
	
	public Position_of(Expression<Unit> e) {
		this.subExpressions.add(e);
	}

	@Override
	public Vector getValue() {
		return this.subExpressions.get(0).getValue().getPosition().floor();
	}

}
