package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.UnitExpression;

public class Attack extends Action{
	
	public Attack(UnitExpression unit){
		super.setTarget(unit);
	}

	@Override
	public void executeSpecific() {

		super.getActor().attack((Unit) super.getTarget().getValue());
		
	}

	@Override
	public boolean complete() {
		if (this.getTask().getUnit().getAttackCooldown()<=0)
			return true;
		return false;
	}

}
