package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public class Follow extends Action{

	public Follow(Expression<Unit> unit){
		super.setTarget(unit);
	}

	@Override
	public void executeSpecific() {

		super.getActor().startFollow((Unit) super.getTarget().getValue());
	}


}
