package hillbillies.model.statement;

import hillbillies.model.expression.*;

public class While extends SuperStatement{

	public While(Expression<Boolean> condition2, Statement b){
		this.setBody(b);
		this.setCondition(condition2);
	}
	
	@Override
	public void execute() {
//		this.getCondition().setTask(super.task);
//		this.getBody().setTask(super.task);
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		while ((boolean) this.getCondition().getValue() && this.isInLoop()){
			super.task.countDown();
//			this.body.setSuperStatment(this);
			this.getBody().execute();
			if (super.getTask().getCounter()<1)
				return;
		}
		super.setCompleted(true);
	}
	
	@Override
	public void stopLoop(){
		this.setInLoop(false);
	}
	
//	@Override
//	public void reset(){
//		this.setCompleted(false);
//		this.getBody().setCompleted(false);
//	}
		
	public Expression<Boolean> getCondition() {
		return condition;
	}
	public void setCondition(Expression<Boolean> condition) {
		this.Expressions.add(condition);
		this.condition = condition;
	}

	public Statement getBody() {
		return body;
	}
	public void setBody(Statement body) {
		this.Substatements.add(body);
		this.body = body;
		body.setSuperStatment(this);
	}
	
	public boolean isInLoop() {
		return inLoop;
	}

	public void setInLoop(boolean inLoop) {
		this.inLoop = inLoop;
	}

	private boolean inLoop = true;
	private Expression<Boolean> condition;
	private Statement body;
	

}
