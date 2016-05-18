package hillbillies.model.expression;

import hillbillies.model.*;
import hillbillies.model.statement.WrapStatement;

public class Selected extends PositionExpression implements IBasicExpression{
	
	public Selected() {
	}

	@Override
	public Vector getValue() {
		if(getTask().getUnit() == null || getTask().getUnit().getWorld() == null){
			return null;
		}
		World world = getTask().getUnit().getWorld();
		if(world.isValidPosition(getTask().getSelected())){
			return this.getTask().getSelected();
		}
		return null;
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
