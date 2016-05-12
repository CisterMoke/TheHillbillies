package hillbillies.model.expression;

import hillbillies.model.Vector;

public class Here extends PositionExpression{
	public Here() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector getValue() {
		return this.getUnit().getPosition().floor();
	}
	
}
