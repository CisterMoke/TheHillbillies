package hillbillies.model.expression;

import java.util.ArrayList;

public interface IComposedExpression <I>{
	public default boolean hasSubExpression(){
		return true;
	}
	
	public ArrayList<I> getSubExpressions();
}
