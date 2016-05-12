package hillbillies.model.expression;

import hillbillies.model.Vector;

public class Is_solid extends ComposedExpression<Boolean,Vector>{
	public Is_solid(Expression<Vector> e) {
		this.subExpressions.add(e);
		}

		@Override
		public Boolean getValue() {
			return !this.getUnit().getWorld().getBlockAtPos(this.subExpressions.get(0).getValue()).isSolid();
		}

}
