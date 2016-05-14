package hillbillies.model.statement;

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
		for (Statement sub : Substatements){
			sub.setTask(newtask);
		}
		for (Expression<?> exp : Expressions){
			exp.setTask(newtask);
		}
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
	
	public Map<String, Expression<?>> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Expression<?>> variables) {
		this.variables = variables;
	}
	
	public void addVariable(String name, Expression<?> value){
		this.variables.put(name, value);
	}
	
	public Expression<?> readVariable(String name){
		if (this.getVariables().containsKey(name))
			return this.getVariables().get(name);
		else {
			if (this.getWrapStatement()!=null)
				return this.getWrapStatement().readVariable(name);
			else {
				System.out.println("Unassigned variable: " + name);
				return null;
			}
		}
	}
	
	public void initSupers(){
		for (Statement sub : Substatements){
			sub.setWrapStatment(this);
		} 
	}

	private Map<String, Expression<?>> variables;
}

