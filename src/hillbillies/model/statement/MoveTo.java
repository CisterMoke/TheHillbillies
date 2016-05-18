package hillbillies.model.statement;

import hillbillies.model.Vector;
import hillbillies.model.expression.PositionExpression;

public class MoveTo extends Action{

	public MoveTo(PositionExpression position){
		super.setTarget(position);
	}

	@Override
	public void executeSpecific() {
		super.getActor().move2((Vector) super.getTarget().getValue());
	}

	@Override
	public boolean complete() {
		if (this.getTask().getUnit().getFinTarget()==null || this.getTask().getUnit().getPosition()==this.getTask().getUnit().getFinTarget())
			return true;
		return false;
	}
}
