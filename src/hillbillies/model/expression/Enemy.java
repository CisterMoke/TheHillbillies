package hillbillies.model.expression;

import hillbillies.model.Unit;

public class Enemy extends UnitExpression{
	public Enemy() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Unit getValue() {
		return this.getUnit().getClosestEnemy();
	}
}
