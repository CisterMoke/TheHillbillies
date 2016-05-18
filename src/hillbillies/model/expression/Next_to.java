package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.*;
import hillbillies.model.statement.WrapStatement;

public class Next_to extends PositionExpression implements IComposedExpression<Expression<?>>{
	
	public Next_to(Expression<?> e) {
		if(!(e instanceof Read)){
			PositionExpression exp = (PositionExpression) e;
			this.subExpressions.add(exp);
		}
		else subExpressions.add(e);
	}

	public void setValue(){
		World world = this.getUnit().getWorld();
		ArrayList<Block> neighbours = world.getDirectlyAdjacent(world.getBlockAtPos((Vector)this.subExpressions.get(0).getValue()));
		ArrayList<Block> path = getUnit().getClosestPath(neighbours);
		this.value = (path.isEmpty() ? null : path.get(path.size() - 1).getLocation());
	}
	
	@Override
	public Vector getValue() {
		this.setValue();
		return value;
	}
	
	private Vector value=null;
	
	@Override
	public ArrayList<Expression<?>> getSubExpressions() {
		return new ArrayList<Expression<?>>(subExpressions);
	}
	
	private ArrayList<Expression<?>> subExpressions = new ArrayList<Expression<?>>();

	@Override
	public void setTask(Task task) {
		this.assignedTask = task;
		for(Expression<?> e : subExpressions)
			e.setTask(task);
	}

	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement=newstat;
		for (Expression<?> expr : subExpressions){
			expr.setWrapStatement(newstat);
		}
	}

	@Override
	public boolean hasNullExpressions(){
		for (Expression<?> e : subExpressions){
			if(e.hasNullExpressions()){
				return true;
			}
		}
		return this.getValue()==null;
	}

}
