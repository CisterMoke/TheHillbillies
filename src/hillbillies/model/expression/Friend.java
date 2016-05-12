package hillbillies.model.expression;

import hillbillies.model.Unit;

public class Friend extends UnitExpression{
	public Friend() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Unit getValue() {
		return this.getUnit().getClosestFriend();
	}

}
