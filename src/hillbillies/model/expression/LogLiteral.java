package hillbillies.model.expression;

import hillbillies.model.Vector;

public class LogLiteral extends PositionExpression{
	
	public LogLiteral() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector getValue() {
		return this.getUnit().getClosestLog().getPosition().floor();
	}
	
	

}
