package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.*;

public class MoveTo extends Action{

	public MoveTo(Expression<?> position){
		if(!(position instanceof Read)){
			PositionExpression e = (PositionExpression) position;
			setTarget(e);
		}
		else super.setTarget(position);
	}

	@Override
	public void executeSpecific() {
		super.getActor().move2((Vector) super.getTarget().getValue());
	}
}
