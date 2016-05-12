package hillbillies.model.expression;

import hillbillies.model.Vector;

public class BoulderLiteral extends PositionExpression{

	public BoulderLiteral(){
		
	}

	@Override
	public Vector getValue() {
		return this.getUnit().getClosestBoulder().getPosition().floor();
	}
	
	
}
