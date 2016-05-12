package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public abstract class Action extends SimpleStatement{
	
	@Override
	public abstract void execute();

	public Unit getActor() {
		return Actor;
	}

	public void setActor(Unit actor) {
		Actor = actor;
	}

	public Expression<?> getTarget() {
		return Target;
	}

	public void setTarget(Expression<?> target) {
		Target = target;
	}

	private Unit Actor;
	private Expression<?> Target;
}
