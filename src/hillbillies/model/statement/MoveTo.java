package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;

public class MoveTo extends Action{

	public MoveTo(Expression<Vector> position){
		super.setTarget(position);
	}

	@Override
	public void execute() {
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		this.getTarget().setTask(super.getTask());
		super.getActor().move2((Vector) super.getTarget().getValue());
		if (getActor().getFinTarget()==null)
			super.setCompleted(true);
	}
}
