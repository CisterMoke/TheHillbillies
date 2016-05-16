package hillbillies.model;

import java.util.*;

/**
 * Class representing a Scheduler
 * @author Joost Croonen & Ruben Dedoncker
 *
 */
public class Scheduler {
	/**
	 * Initialize a Scheduler.
	 */
	public Scheduler() {	
	}
	/**
	 * Schedule a given Task.
	 * @param 	task
	 * 			The given Task to be Scheduled.
	 * @post	The Task is added to the set of Tasks of this Scheduler
	 * 			and this Scheduler is added to the set of Schedulers of the
	 * 			given Task.
	 * 			| getTasks().contains(task) && task.getSchedulers().contains(this)
	 */
	public void scheduleTask(Task task){
		task.addScheduler(this);
		this.tasks.add(task);
	}
	/**
	 * Remove a Task from this Scheduler.
	 * @param 	task
	 * 			The given Task to be removed.
	 * @post	The given Task is removed from this Scheduler's set of Tasks
	 * 			and this Scheduler is removed from the given Tasks set of
	 * 			Schedulers.
	 * 			| !getTasks().contains(task) && !task.getSchedulers().contains(this)
	 * @effect	If the given Task has a Unit assigned to it, the Unit will remove
	 * 			the given Task.
	 * 			| if (task.getUnit() != null)
	 * 			|	then task.getUnit().removeTask()
	 */
	public void removeTask(Task task){
		if(!task.inSchedulerSet(this))
			return;
		this.tasks.remove(task);
		task.removeScheduler(this);
		if(task.getUnit() != null)
			task.getUnit().removeTask();
	}
	/**
	 * 
	 * @return
	 */
	public Set<Task> getTasks(){
		return new HashSet<Task> (this.tasks);
	}
	
	public boolean inTaskSet(Task task){
		return tasks.contains(task);
	}
	
	public boolean inTaskSet(Collection<Task> c){
		return tasks.containsAll(c);
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
			if(!task.isAssigned())
				return task;
		}
		return null;
	}
	
	private Comparator<Task> getPriorityComparator(){
		return new Comparator<Task>(){

			@Override
			public int compare(Task task1, Task task2) {
				return (int) Math.signum(task2.getPriority() - task1.getPriority());
			}
			
		};
	}

	
//	public Task getHighestUnassignedTask(){
//		Iterator<Task> iter = iterator();
//		Task task = null;
//		while(iter.hasNext() && task == null){
//			Task temp = iter.next();
//			if(!temp.isAssigned()){
//				task = temp;
//			}
//		}
//		return task;
//	}
	
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
