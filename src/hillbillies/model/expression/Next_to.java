package hillbillies.model.expression;

import java.util.ArrayList;

import hillbillies.model.*;

public class Next_to extends ComposedExpression<Vector,Vector>{
	
	public Next_to(Expression<Vector> e) {
		this.subExpressions.add(e);
	}

	@Override
	public Vector getValue() {
		World world = this.getUnit().getWorld();
		ArrayList<Block> neigbours = world.getDirectlyAdjacent(world.getBlockAtPos(this.subExpressions.get(0).getValue()));
		int idx = (int) Math.floor(Math.random()*neigbours.size());
		return neigbours.get(idx).getLocation();
	}
	
	

}
