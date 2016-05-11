package hillbillies.model.expression;

public abstract class ComposedExpression<O> extends Expression<O>{
	
	public Expression<?>[] subExpressions;
	
	@Override
	public boolean hasSubExpression(){
		return true;
	}
}
