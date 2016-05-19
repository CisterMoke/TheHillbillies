package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.*;

public class Follow extends Action{

	public Follow(Expression<?> unit){
		if(!(unit instanceof Read)){
			UnitExpression e = (UnitExpression) unit;
			setTarget(e);
		}
		else super.setTarget(unit);
	}

	@Override
	public void executeSpecific() {

		super.getActor().startFollow((Unit) super.getTarget().getValue());
	}

	@Override
	public boolean actionDone() {
		return true;
	}


}
