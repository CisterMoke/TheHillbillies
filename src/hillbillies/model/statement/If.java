package hillbillies.model.statement;

import hillbillies.model.expression.*;

public class If extends Statement{

	public If(Expression<Boolean> condition2, Statement IB, Statement EB){
		condition2.setTask(super.getTask());
		this.setCondition(condition2);
		this.setIfBody(IB);
		this.setElseBody(EB);

	}
	@Override
	public void execute(){
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		if ((boolean) this.getCondition().getValue())
			this.getIfBody().execute();
		else
			this.getElseBody().execute();
		super.setCompleted(true);
	}

	public Expression<Boolean> getCondition() {
		return condition;
	}
	
	public void setCondition(Expression<Boolean> condition2) {
		this.condition = condition2;
	}

	public Statement getIfBody() {
		return IfBody;
	}

	public void setIfBody(Statement ifBody) {
		IfBody = ifBody;
	}

	public Statement getElseBody() {
		return ElseBody;
	}

	public void setElseBody(Statement elseBody) {
		ElseBody = elseBody;
	}

	private Expression<Boolean> condition;
	private Statement IfBody;
	private Statement ElseBody;
}

