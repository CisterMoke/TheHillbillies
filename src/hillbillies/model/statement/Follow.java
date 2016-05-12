package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public class Follow extends Action{

	public Follow(Expression<?> unit){
		unit.setTask(super.getTask());
		super.setTarget(unit);
	}

	@Override
	public void execute() {
		if (super.getCompleted())
			return;
		super.getActor().move2(((Unit) super.getTarget().getValue()).getPosition());
		super.setCompleted(true);
	}


}
