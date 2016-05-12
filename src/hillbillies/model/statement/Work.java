package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;

public class Work extends Action{

	public Work(Expression<?> position){
		position.setTask(super.getTask());
		super.setTarget(position);
	}

	@Override
	public void execute() {
		if (super.getCompleted())
			return;
		super.getActor().workAt((Vector) super.getTarget().getValue());
		super.setCompleted(true);
	}

}
