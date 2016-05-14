package hillbillies.model.statement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import hillbillies.model.Task;
import hillbillies.model.expression.Expression;
import hillbillies.model.expression.Read;

public abstract class Statement {
	
	public void reset(){
		this.setCompleted(false);
	}
	
	protected WrapStatement wrapStatement = null;
	
	public void setWrapStatment(WrapStatement stat){
		this.wrapStatement = stat;
	}
	
	public WrapStatement getWrapStatement(){
		return this.wrapStatement;
	}
	
	public abstract void execute();
	
	public boolean getCompleted(){
		return this.completed;
	}
	
	protected ArrayList<Expression<?>> Expressions = new ArrayList<Expression<?>>();
	
	public void addExression(Expression<?> e){
		this.Expressions.add(e);
		if (e instanceof Read){
			((Read) e).setStatement(this.getWrapStatement());
		}
	}
	
	public void setCompleted(boolean value){
		this.completed=value;
	}

	protected boolean completed=false;
	
	public void setTask(Task newtask){
		this.task = newtask;
		for (Expression<?> exp : Expressions){
			exp.setTask(newtask);
		}
	}
	
	protected boolean hasNullExpressions(){
		for(Expression<?> e : Expressions){
			if(e.getValue() == null)
				return true;
		}
		return false;
	}
	
	public Task getTask(){
		return task;
	}
	
	protected Task task;
}
