package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.*;
import hillbillies.model.statement.WrapStatement;

public class Next_to extends PositionExpression implements IComposedExpression<PositionExpression>{
	
	public Next_to(PositionExpression e) {
		this.subExpressions.add(e);
	}

	public void setValue(){
		World world = this.getUnit().getWorld();
		ArrayList<Block> neighbours = world.getDirectlyAdjacent(world.getBlockAtPos(this.subExpressions.get(0).getValue()));
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
	public ArrayList<PositionExpression> getSubExpressions() {
		return new ArrayList<PositionExpression>(subExpressions);
	}
	
	private ArrayList<PositionExpression> subExpressions = new ArrayList<PositionExpression>();

	@Override
	public void setTask(Task task) {
		this.assignedTask = task;
		for(PositionExpression e : subExpressions)
			e.setTask(task);
	}

	@Override
	public void setWrapStatement(WrapStatement newstat) {
		this.statement=newstat;
		for (PositionExpression expr : subExpressions){
			expr.setWrapStatement(newstat);
		}
	}

	@Override
	public boolean hasNullExpressions(){
		for (PositionExpression e : subExpressions){
			if(e.hasNullExpressions()){
				return true;
			}
		}
		return this.getValue()==null;
	}

}
