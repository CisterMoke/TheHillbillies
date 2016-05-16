package hillbillies.model.expression;

public abstract class BasicExpression<O> extends Expression<O>{
	
	@Override
	public boolean hasSubExpression(){
		return false;
	}
	
	@Override
	public boolean hasNullExpressions(){
		return getValue() == null;
	}
	
}
