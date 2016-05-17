package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.*;

public class Next_to extends ComposedExpression<Vector,Vector>{
	
	public Next_to(Expression<Vector> e) {
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

}
