package hillbillies.model.expression;

public interface IBasicExpression{
	
	public default boolean hasSubExpression(){
		return false;
	}
	
//	@Override
//	public boolean hasNullExpressions(){
//		return getValue() == null;
//	}
	
}
