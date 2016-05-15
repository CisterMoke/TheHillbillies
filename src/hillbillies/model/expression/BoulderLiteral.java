package hillbillies.model.expression;

import hillbillies.model.Vector;

public class BoulderLiteral extends PositionExpression{

	public BoulderLiteral(){
		
	}

	@Override
	public Vector getValue() {
		if (!(this.getUnit().getClosestBoulder()==null))
			return this.getUnit().getClosestBoulder().getPosition().floor();
		else return null;
	}
	
	
}
