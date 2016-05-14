package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public abstract class Action extends BasicStatement{

	public Unit getActor() {
		return this.getTask().getUnit();
	}

	public Expression<?> getTarget() {
		return Target;
	}
	
	public void setTarget(Expression<?> target) {
		super.Expressions.add(target);
		Target = target;
	}
	
	@Override	
	public void execute(){
		System.out.println(this.getClass());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
//		this.getTarget().setTask(super.getTask());
		super.task.countDown();
		this.executeSpecific();
	}

	private Expression<?> Target;
}
