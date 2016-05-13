package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public class Attack extends Action{
	
	public Attack(Expression<Unit> unit){
		super.setTarget(unit);
	}

	@Override
	public void execute() {
		this.getTarget().setTask(super.getTask());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		super.getActor().attack((Unit) super.getTarget().getValue());
		if (getActor().getAttackCooldown()<0)
			setCompleted(true);
		
	}

}
