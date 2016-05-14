package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public class Follow extends Action{

	public Follow(Expression<Unit> unit){
		super.setTarget(unit);
	}

	@Override
	public void execute() {
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		this.getTarget().setTask(super.getTask());
		super.getActor().startFollow((Unit) super.getTarget().getValue());
		if (getActor().getFinTarget()==null)
			super.setCompleted(true);
	}


}
