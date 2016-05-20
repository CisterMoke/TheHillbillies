package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.Basic;
import hillbillies.model.Task;
import hillbillies.model.Vector;
import hillbillies.model.World;
import hillbillies.model.statement.WrapStatement;

public class PositionLiteral extends PositionExpression implements IBasicExpression{
	
	public PositionLiteral(Vector v) {
		this.pos = v;
	}
	
	private Vector pos;
	
	@Basic
	public Vector getPos(){
		return pos;
	}

	@Override
	public Vector getValue() {
		World world = getTask().getUnit().getWorld();
		if(world == null || !world.isValidPosition(pos))
			return null;
		return this.pos;
	}

	@Override
	public void setTask(Task task) {
		this.assignedTask = task;
		
	}

	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement = newstat;
		
	}

	@Override
	public boolean hasNullExpressions() {
		return getValue() == null;
	}
}
