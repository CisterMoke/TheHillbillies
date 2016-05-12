package hillbillies.model.expression;

import hillbillies.model.Unit;

public class Brackets extends ComposedExpression<Expression<?>, ?>{
	public Brackets(Expression<?> e){
		this.subExpressions.add(e);
	}
	
	public Expression<?>[] getExp(){
		return this.subExpressions;
	}

	@Override
	public Expression<?> getValue() {
		return this.subExpressions[0];
	}
	
	

}
