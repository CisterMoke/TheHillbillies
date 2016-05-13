package hillbillies.model.statement;

import hillbillies.model.expression.*;

public class While extends Statement{

	public While(Expression<Boolean> condition2, Statement b){
		this.setBody(b);
		this.setCondition(condition2);
	}
	
	@Override
	public void execute() {
		this.getCondition().setTask(super.task);
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		super.task.setLoopDepth(super.task.getLoopDepth()+1);
		Integer depth = super.task.getLoopDepth();
		while ((boolean) this.getCondition().getValue() && depth==super.task.getLoopDepth()){
			this.body.setSuperStatment(this);
			this.getBody().execute();
			if (super.getTask().getCounter()<1)
				return;
		}
		super.setCompleted(true);
	}
	
	@Override
	public void reset(){
		this.setCompleted(false);
		this.getBody().setCompleted(false);
	}
	
	public Expression<Boolean> getCondition() {
		return condition;
	}
	public void setCondition(Expression<Boolean> condition) {
		this.condition = condition;
	}

	public Statement getBody() {
		return body;
	}
	public void setBody(Statement body) {
		this.body = body;
	}

	private Expression<Boolean> condition;
	private Statement body;
	

}
