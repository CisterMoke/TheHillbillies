package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;
import hillbillies.model.expression.PositionExpression;
import hillbillies.model.expression.Read;

public class Work extends Action{

	public Work(Expression<?> position){
		if(!(position instanceof Read)){
			PositionExpression e = (PositionExpression) position;
			setTarget(e);
		}
		else super.setTarget(position);
	}

	@Override
	public void executeSpecific() {
		super.getActor().workAt((Vector) super.getTarget().getValue());
	}

	@Override
	public boolean actionDone() {
		return true;
	}

}
