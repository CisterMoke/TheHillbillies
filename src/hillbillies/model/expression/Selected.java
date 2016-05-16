package hillbillies.model.expression;

import hillbillies.model.*;

public class Selected extends PositionExpression{
	
	public Selected() {
	}

	@Override
	public Vector getValue() {
		if(getTask().getUnit() == null || getTask().getUnit().getWorld() == null)
			return null;
		World world = getTask().getUnit().getWorld();
		if(world.isValidPosition(getTask().getSelected()))
			return this.getTask().getSelected();
		return null;
	}

}
