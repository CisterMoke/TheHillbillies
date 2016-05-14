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
		super.Expressions.add(target);
	}
	
	@Override	
	public void execute(){
		System.out.println(this.getClass());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
//		this.getTarget().setTask(super.getTask());
		if(hasNullExpressions()){
			getTask().getActivity().setCompleted(true);
			return;
		}
		super.task.countDown();
		this.executeSpecific();
	}
}
