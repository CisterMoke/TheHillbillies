package hillbillies.model;

import java.util.*;

public class Scheduler {
	public Scheduler() {	
	}
	
	public void scheduleTask(Task task){
		task.addScheduler(this);
		this.tasks.add(task);
	}
	
	public void removeTask(Task task){
		if(!task.getSchedulers().contains(this))
			return;
		this.tasks.remove(task);
		task.removeScheduler(this);
		task.getUnit().removeTask();
	}
		
	public Set<Task> getTasks(){
		return this.tasks;
	}
	
//	public void assignTask(Task task, Unit unit){
//		if(!this.canBeAssigned(unit) || !task.getSchedulers().contains(this))
//			return;
//		task.setUnit(unit);
//		unit.setTask(task);		
//	}
	
//	public boolean canBeAssigned(Unit unit){
//		return (this.getFaction() == unit.getFaction()) && (!unit.isTerminated()) && ((unit.getTask() == null));
//	}
	
//	public Faction getFaction(){
//		return this.faction;
//	}
	
	public Iterator<Task> iterator(){
		ArrayList<Task> taskList = new ArrayList<Task>(tasks);
		taskList.sort(getPriorityComparator());
		return new Iterator<Task>(){
			
			@Override
			public boolean hasNext(){
				return !taskList.isEmpty();
			}

			@Override
			public Task next() {
				Task first = taskList.get(0);
				taskList.remove(0);
				return first;
			}			
		};
	}
	
	public Task getHighestPriority(){
		Iterator<Task> iter = iterator();
		Task task;
		while(iter.hasNext()){
			task = iter.next();
			if(task.getUnit() == null)
				return task;
		}
		return null;
	}
	
	private Comparator<Task> getPriorityComparator(){
		return new Comparator<Task>(){

			@Override
			public int compare(Task task1, Task task2) {
				return (int) Math.signum(task1.getPriority() - task2.getPriority());
			}
			
		};
	}
	
//	public void setTaskPriority(Task task, int priority){
//		if(!task.getSchedulers().contains(this))
//			return;
//		taskMap.remove(task.getPriority());
//		task.setPriority(priority);
//		taskMap.put(task.getPriority(), task);
//	}
	
//	private Faction faction;
	
	private Set<Task> tasks = new HashSet<Task>();

}
