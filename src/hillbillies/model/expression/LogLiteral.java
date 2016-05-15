package hillbillies.model.expression;

import hillbillies.model.Vector;

public class LogLiteral extends PositionExpression{
	
	public LogLiteral() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector getValue() {
		if (!(this.getUnit().getClosestLog()==null))
			return this.getUnit().getClosestLog().getPosition().floor();
		else return null;
	}
	
	

}
