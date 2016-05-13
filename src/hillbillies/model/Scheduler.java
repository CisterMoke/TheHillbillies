package hillbillies.model;

import java.util.*;

public class Scheduler {
	public Scheduler() {	
	}
	
	public void scheduleTask(Task task){
		task.addScheduler(this);
		this.taskMap.put(task.getPriority(), task);
	}
	
	public void removeTask(Task task){
		if(!task.getSchedulers().contains(this))
			return;
		this.taskMap.remove(task.getPriority());
		task.removeScheduler(this);
		task.getUnit().removeTask();
	}
		
	protected Map<Integer, Task> getTaskMap(){
		return this.taskMap;
	}
	
	public Set<Task> getTasks(){
		Set <Task> tasks = new HashSet<Task>();
		for(int key: taskMap.keySet())
			tasks.add(taskMap.get(key));
		return tasks;
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
		ArrayList<Integer> priorities = new ArrayList<Integer>(taskMap.keySet());
		priorities.sort(null);
		return new Iterator<Task>(){
			
			@Override
			public boolean hasNext(){
				return !priorities.isEmpty();
			}

			@Override
			public Task next() {
				int key = priorities.get(0);
				priorities.remove(0);
				return taskMap.get(key);
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
	
//	public void setTaskPriority(Task task, int priority){
//		if(!task.getSchedulers().contains(this))
//			return;
//		taskMap.remove(task.getPriority());
//		task.setPriority(priority);
//		taskMap.put(task.getPriority(), task);
//	}
	
//	private Faction faction;
	
	private Map<Integer, Task> taskMap = new HashMap<Integer, Task>();

}
