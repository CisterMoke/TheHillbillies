package hillbillies.model.expression;

import hillbillies.model.Vector;

public class PositionLiteral extends PositionExpression{
	
	public PositionLiteral(Vector v) {
		this.pos = v;
	}
	
	private Vector pos;

	@Override
	public Vector getValue() {
		return this.pos;
	}
}
