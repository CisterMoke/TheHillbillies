package hillbillies.model.expression;

import hillbillies.model.Unit;

public class Any extends UnitExpression{

	public Any(){		
	}

	
	@Override
	public Unit getValue() {
		return this.getUnit().getClosestUnit();
	}

}
