package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;

public class MoveTo extends Action{

	public MoveTo(Expression<?> position){
		position.setTask(super.getTask());
		super.setTarget(position);
	}

	@Override
	public void execute() {
		if (super.getCompleted())
			return;
		super.getActor().move2((Vector) super.getTarget().getValue());
		super.setCompleted(true);
	}
}
