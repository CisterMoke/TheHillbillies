package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;

public class Work extends Action{

	public Work(Expression<Vector> position){
		super.setTarget(position);
	}

	@Override
	public void execute() {
		this.getTarget().setTask(super.getTask());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		super.getActor().workAt((Vector) super.getTarget().getValue());
		super.setCompleted(true);
	}

}
