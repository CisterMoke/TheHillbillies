package hillbillies.model.expression;

import java.util.ArrayList;

public interface IComposedExpression <I>{
	
//	protected ArrayList<Expression<I>> subExpressions = new ArrayList<Expression<I>>();

//	public void setWrapStatement(WrapStatement newstat) {
//		this.statement=newstat;
//		if (this.hasSubExpression()){
//			for (Expression<I> expr : subExpressions){
//				expr.setWrapStatement(newstat);
//			}
//		}
//	}
//	
	public default boolean hasSubExpression(){
		return true;
	}
	
	public ArrayList<I> getSubExpressions();
	
//	public default void setTask(Task task){
//		this.assignedTask = task;
//		if (this.hasSubExpression()){
//			for (Expression<I> expr : subExpressions){
//				expr.setTask(task);
//			}
//		}
//	}
	
//	public boolean hasNullExpressions(){
//		for (Expression<I> e : subExpressions){
//			if(e.hasNullExpressions()){
//				return true;
//			}
//		}
//		return this.getValue()==null;
//			
//	}
}
