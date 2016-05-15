package hillbillies.model.expression;

import hillbillies.model.Vector;

public class WorkshopLiteral extends PositionExpression{
	
	public WorkshopLiteral() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector getValue() {
		if (!(this.getTask().getUnit().getClosestWorkshop()==null))
			return this.getTask().getUnit().getClosestWorkshop().getLocation();
		else return null;
	}

}
