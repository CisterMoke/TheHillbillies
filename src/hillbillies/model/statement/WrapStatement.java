package hillbillies.model.statement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hillbillies.model.Task;
import hillbillies.model.expression.Expression;

public abstract class WrapStatement extends Statement{
	
	protected Set<Statement> Substatements = new HashSet<Statement>();
	
	@Override
	public void setTask(Task newtask){
		this.task=newtask;
//		for (Statement sub : Substatements){
//			sub.setTask(newtask);
//		}
//		for (Expression<?> exp : Expressions){
//			exp.setTask(newtask);
//		}
		
	}
	
	@Override 
	public void reset(){
		this.setCompleted(false);
		for (Statement sub : Substatements){
			sub.setCompleted(false);
		} 
	}
	
	public void stopLoop(){
		if (this.getWrapStatement()!=null)
			this.getWrapStatement().stopLoop();
		else {System.out.println("place break inside a loop");}
	}
	
	@Override
	public void initialise(Task newTask){
		System.out.println("init");
		this.setTask(newTask);
		for (Statement sub : Substatements){
			sub.setWrapStatement(this);
			sub.initialise(newTask);
		} 
		for (Expression<?> exp : Expressions){
			exp.setTask(newTask);
			exp.setStatement(this);
		}
		if (!this.getCompletedTotal().containsKey(this.getTask()))
			this.setCompleted(false);
	}
	

	public void addVariable(String name, Expression<?> value){
		this.getWrapStatement().addVariable(name, value);	
		}
	
	public Expression<?> readVariable(String name){
		return this.getWrapStatement().readVariable(name);
	}

}

