package hillbillies.model.expression;

import hillbillies.model.Vector;

public class Selected extends PositionExpression{
	
	public Selected() {
	}

	@Override
	public Vector getValue() {
		return this.getTask().getSelected();
	}

}
