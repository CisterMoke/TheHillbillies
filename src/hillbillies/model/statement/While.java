package hillbillies.model.statement;

import hillbillies.model.expression.*;

public class While extends WrapStatement{

	public While(Expression<?> condition2, Statement b){
		if(!(condition2 instanceof Read)){
			BooleanExpression e = (BooleanExpression) condition2;
			setCondition(e);
		}		
		else setCondition(condition2);
		this.setBody(b);
	}
	
	@Override
	public void execute() {
//		System.out.println(this.getClass());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		if(hasNullExpressions()){
			getTask().getActivity().setCompleted(true);
			return;
		}
		boolean prevCompleted=true;
		while ((boolean) this.getCondition().getValue() && this.isInLoop() && prevCompleted){
			this.getBody().reset();
			super.task.countDown();
			this.getBody().execute();
			if (super.getTask().getCounter()<1)
				return;
			prevCompleted=this.getBody().getCompleted();
			
		}
		super.setCompleted(!(boolean)this.getCondition().getValue() || !this.isInLoop());
	}
	
	@Override
	public boolean stopLoop(){
		this.setInLoop(false);
		return true;
	}
	
	public Expression<?> getCondition() {
		return condition;
	}
	public void setCondition(Expression<?> condition) {
		super.addExpression(condition);
		this.condition = condition;
	}

	public Statement getBody() {
		return body;
	}
	public void setBody(Statement body) {
		this.Substatements.add(body);
		this.body = body;
	}
	
	public boolean isInLoop() {
		return inLoop;
	}

	public void setInLoop(boolean inLoop) {
		this.inLoop = inLoop;
	}

	private boolean inLoop = true;
	private Expression<?> condition;
	private Statement body;
	

}
