package hillbillies.model.statement;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.core.IsInstanceOf;

import hillbillies.model.Task;
import hillbillies.model.expression.Expression;
import hillbillies.model.expression.Read;

public abstract class Statement {
	
	public void reset(){
		this.setCompleted(false);
	}
	
	protected SuperStatement superStatement = null;
	
	public void setSuperStatment(SuperStatement stat){
		this.superStatement = stat;
	}
	
	public SuperStatement getSuperStatement(){
		return this.superStatement;
	}
	
	public abstract void execute();
	
	public boolean getCompleted(){
		return this.completed;
	}
	
	protected Set<Expression<?>> Expressions = new HashSet<Expression<?>>();
	
	public void addExression(Expression<?> e){
		this.Expressions.add(e);
		if (e instanceof Read){
			((Read) e).setStatement(this.getSuperStatement());
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
	
	public Task getTask(){
		return task;
	}
	
	protected Task task;
}
