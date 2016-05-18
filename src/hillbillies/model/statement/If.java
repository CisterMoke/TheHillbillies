package hillbillies.model.statement;

import hillbillies.model.expression.*;

public class If extends WrapStatement{

	public If(BooleanExpression condition2, Statement IB, Statement EB){
		this.setCondition(condition2);
		this.setIfBody(IB);
		this.setElseBody(EB);
	}
	@Override
	public void execute(){
		System.out.println(this.getClass());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		if(hasNullExpressions()){
			getTask().getActivity().setCompleted(true);
			return;
		}
		super.task.countDown();
		if ((boolean) this.getCondition().getValue()){
			this.getIfBody().execute();
			this.setCompleted(this.getIfBody().getCompleted());
		}
		else{
			if (this.getElseBody()!=null){
				this.getElseBody().execute();
				this.setCompleted(this.getElseBody().getCompleted());
			}
			else{this.setCompleted(true);}
		}
		
	}

	public Expression<Boolean> getCondition() {
		return condition;
	}
	
	public void setCondition(Expression<Boolean> condition2) {
		super.addExpression(condition2);
		this.condition = condition2;
	}

	public Statement getIfBody() {
		return IfBody;
	}

	public void setIfBody(Statement ifBody) {
		IfBody = ifBody;
		this.Substatements.add(ifBody);
	}

	public Statement getElseBody() {
		return ElseBody;
	}

	public void setElseBody(Statement elseBody) {
		ElseBody = elseBody;
		if (elseBody!=null)
			this.Substatements.add(elseBody);
	}

	private Expression<Boolean> condition;
	private Statement IfBody;
	private Statement ElseBody;
}

