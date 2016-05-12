package hillbillies.model.expression;

public class Or extends ComposedExpression<Boolean,Boolean>{
	
	public Or(Expression<Boolean> left, Expression<Boolean> right){
		this.subExpressions.add(left);
		this.subExpressions.add(right);
	}

	@Override
	public Boolean getValue() {
		return this.subExpressions.get(0).getValue() || this.subExpressions.get(0).getValue();
	}

}
