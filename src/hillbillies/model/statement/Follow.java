package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.UnitExpression;

public class Follow extends Action{

	public Follow(UnitExpression unit){
		super.setTarget(unit);
	}

	@Override
	public void executeSpecific() {

		super.getActor().startFollow((Unit) super.getTarget().getValue());
	}

	@Override
	public boolean complete() {
		if (this.getTask().getUnit().endFollow())
			return true;
		return false;
	}


}
