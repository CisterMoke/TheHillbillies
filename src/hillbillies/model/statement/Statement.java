package hillbillies.model.statement;

import hillbillies.model.Task;

public abstract class Statement {
	
	
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
