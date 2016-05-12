package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public class Attack extends Action{
	
	public Attack(Expression<?> unit){
		unit.setTask(super.getTask());
		super.setTarget(unit);
	}

	@Override
	public void execute() {
		if (super.getCompleted())
			return;
		super.getActor().attack((Unit) super.getTarget().getValue());
		super.setCompleted(true);
	}

}
