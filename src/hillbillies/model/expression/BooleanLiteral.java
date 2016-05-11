package hillbillies.model.expression;

public class BooleanLiteral extends BasicExpression<Boolean>{
	
	public BooleanLiteral(boolean value){
		this.value = value;
	}
	
	private final boolean value;

	@Override
	public Boolean getValue() {
		return this.value;
	}

}
