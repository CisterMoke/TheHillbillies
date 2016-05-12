package hillbillies.model.expression;

import java.util.ArrayList;

public abstract class ComposedExpression<O,I> extends Expression<O>{
	
	public ArrayList<Expression<I>> subExpressions = new ArrayList<Expression<I>>();
	
	@Override
	public boolean hasSubExpression(){
		return true;
	}
}
