package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;

public class MoveTo extends Action{

	public MoveTo(Expression<Vector> position){
		super.setTarget(position);
	}

	@Override
	public void executeSpecific() {

		super.getActor().move2((Vector) super.getTarget().getValue());
	}
}
