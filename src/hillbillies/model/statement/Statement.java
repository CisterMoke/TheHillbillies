package hillbillies.model.statement;

import hillbillies.model.Task;

public abstract class Statement {
	
	public void reset(){
		this.setCompleted(false);
	}
	
	protected Statement superStatement = null;
	
	public void setSuperStatment(Statement stat){
		this.superStatement = stat;
	}
	
	public Statement getSuperStatement(){
		return this.superStatement;
	}
	
	public abstract void execute();
	
	public boolean getCompleted(){
		return this.completed;
	}
	
	public void setCompleted(boolean value){
		this.completed=value;
	}

	protected boolean completed=false;
	
	public void setTask(Task newtask){
		this.task = newtask;
	}
	
	public Task getTask(){
		return task;
	}
	
	protected Task task;
}
