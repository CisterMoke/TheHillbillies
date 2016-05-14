package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;

public class MoveTo extends Action{

	public MoveTo(Expression<Vector> position){
		super.setTarget(position);
	}

	@Override
	public void executeSpecific() {
//		if (super.getCompleted() || super.getTask().getCounter()<1)
//			return;
//		super.task.countDown();
//		this.getTarget().setTask(super.getTask());
		System.out.println(this.getActor());
		super.getActor().move2((Vector) super.getTarget().getValue());
		System.out.println(getActor().getFinTarget());
		if (getActor().getFinTarget()==null)
			super.setCompleted(true);
	}
}
