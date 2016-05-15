package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;

public class Work extends Action{

	public Work(Expression<Vector> position){
		super.setTarget(position);
	}

	@Override
	public void executeSpecific() {
		super.getActor().workAt((Vector) super.getTarget().getValue());
	}

	@Override
	public boolean complete() {
		if (this.getTask().getUnit().getWorkTime()<=0)
			return true;
		return false;
	}

}
