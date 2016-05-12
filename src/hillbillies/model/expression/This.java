package hillbillies.model.expression;

import hillbillies.model.Unit;

public class This extends UnitExpression{
	
	public This() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Unit getValue() {
		return this.getTask().getUnit();
	}

}
