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
		ArrayList<Block> accesible = new ArrayList<Block>();
		for (Block block : neighbours){
			if (world.isWalkable(block)){
				accesible.add(block);
			}
		}
		int idx = (int) Math.floor(Math.random()*accesible.size());
		this.value = this.value==null? accesible.get(idx).getLocation() : this.value;
	}
	
	@Override
	public Vector getValue() {
		this.setValue();
		return value;
	}
	
	private Vector value=null;

}
