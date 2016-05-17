package hillbillies.model.statement;

import java.util.*;

import hillbillies.model.Task;
import hillbillies.model.expression.Expression;
import hillbillies.model.expression.Read;
import hillbillies.model.expression.SuperExpression;

public abstract class Statement {
	
	public void reset(){
		setCompleted(false);
	}
	
	protected WrapStatement wrapStatement = null;
	
	public void setWrapStatement(WrapStatement stat){
		this.wrapStatement = stat;
		for (SuperExpression exp : this.Expressions){
			if (this.getWrapStatement()!=null)
				exp.setWrapStatement(this.getWrapStatement());
		}
	}
	
	public boolean isWellFormed(){
		boolean Check = true;
		for (SuperExpression exp : this.Expressions){
			if (exp instanceof Read){
				Check = (exp.getWrapStatement().readVariable(((Read) exp).getName())!=null) && Check;
			}
		}
		return Check;
	}
	
	public WrapStatement getWrapStatement(){
		return this.wrapStatement;
	}
	
	public abstract void execute();
	
	public Boolean getCompleted(){
		return this.completed.get(this.getTask());
	}
	
	public Map<Task, Boolean> getCompletedTotal(){
		return this.completed;
	}
	
	protected ArrayList<SuperExpression> Expressions = new ArrayList<SuperExpression>();
	
	public void addExpression(SuperExpression value){
		this.Expressions.add(value);
	}
	
	public void setCompletedTotal(Map<Task, Boolean> map){
		this.completed=map;
	}
	
	public void setCompleted(Boolean value){
		this.completed.put(this.getTask(), value);
	}
	
	public void terminate(){
		this.completed.remove(task);
	}

	protected Map<Task, Boolean> completed=new HashMap<Task, Boolean>();
	
	public void setTask(Task newtask){
		this.task = newtask;
		
	}
	
	protected boolean hasNullExpressions(){
		for(SuperExpression e : Expressions){
//			if (!this.getTask().getCheckedExpression().contains(e)){
			if(e.hasNullExpressions()){
				return true;
//				}
//			this.getTask().addCheckedExpression(e);
			}
		}
		return false;
	}
	
	public Task getTask(){
		return task;
	}
	
	public void initialise(Task task) {
		this.setTask(task);
		for (SuperExpression exp : Expressions){
			exp.setTask(task);
		}
		if (!this.getCompletedTotal().containsKey(this.getTask()))
			this.setCompleted(false);
	}
	
	protected Task task;
}
