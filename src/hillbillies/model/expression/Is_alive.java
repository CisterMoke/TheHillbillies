package hillbillies.model.expression;

import hillbillies.model.Unit;

public class Is_alive extends ComposedExpression<Boolean, Unit>{
	
	public Is_alive(Expression<Unit> e) {
		this.subExpressions.add(e);
	}

	@Override
	public Boolean getValue() {
		return !this.subExpressions.get(0).getValue().isTerminated();
	}
	
	

}
