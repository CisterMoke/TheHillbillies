package hillbillies.model.statement;

import hillbillies.model.expression.*;

public class While extends ComposedStatement{

	public While(Expression<?> condition2, Statement b){
		condition2.setTask(super.getTask());
		this.setBody(b);
		this.setCondition(condition2);
	}
	
	@Override
	public void execute() {
		while ((boolean) this.getCondition().getValue()){
			this.getBody().execute();
			super.task.countDown();	
			if (super.getCompleted())
				return;
		}
		super.setCompleted(true);
	}
	
	public Expression<?> getCondition() {
		return condition;
	}
	public void setCondition(Expression<?> condition) {
		this.condition = condition;
	}

	public Statement getBody() {
		return body;
	}
	public void setBody(Statement body) {
		this.body = body;
	}

	private Expression<?> condition;
	private Statement body;
	

}
