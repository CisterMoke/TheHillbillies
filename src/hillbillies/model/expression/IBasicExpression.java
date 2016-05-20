package hillbillies.model.expression;

public interface IBasicExpression{
	
	public default boolean hasSubExpression(){
		return false;
	}
}
