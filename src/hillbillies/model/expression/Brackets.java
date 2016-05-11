package hillbillies.model.expression;

public class Brackets extends ComposedExpression<Expression<?>>{
	public Brackets(Expression<?> e){
		this.subExpressions = new Expression<?>[] {e};
	}
	
	public Expression<?>[] getExp(){
		return this.subExpressions;
	}

	@Override
	public Expression<?> getValue() {
		return this.subExpressions[0];
	}
	
	

}
