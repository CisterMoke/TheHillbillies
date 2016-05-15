package hillbillies.model.statement;

import java.util.*;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public abstract class Action extends BasicStatement{

	public Unit getActor() {
		return this.getTask().getUnit();
	}

	public Expression<?> getTarget() {
		return Expressions.get(0);
	}
	
	public void setTarget(Expression<?> target) {
		if(!Expressions.isEmpty())
			Expressions.remove(0);
		super.addExpression(target);
	}
}
