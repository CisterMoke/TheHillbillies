package hillbillies.model.expression;

import hillbillies.model.Vector;
import hillbillies.model.World;

public class PositionLiteral extends PositionExpression{
	
	public PositionLiteral(Vector v) {
		this.pos = v;
	}
	
	private Vector pos;

	@Override
	public Vector getValue() {
		World world = getTask().getUnit().getWorld();
		if(world == null || !world.isValidPosition(pos))
			return null;
		return this.pos;
	}
}
