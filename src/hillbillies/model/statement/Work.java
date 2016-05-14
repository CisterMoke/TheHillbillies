package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;

public class Work extends Action{

	public Work(Expression<Vector> position){
		super.setTarget(position);
	}

	@Override
	public void executeSpecific() {
//		if (super.getCompleted() || super.getTask().getCounter()<1)
//			return;
//		super.task.countDown();
		super.getActor().workAt((Vector) super.getTarget().getValue());
		System.out.println(this.getActor().getWorkTime());
		if (getActor().getWorkTime()<=0)
			super.setCompleted(true);
	}

}
