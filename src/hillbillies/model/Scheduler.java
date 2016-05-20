package hillbillies.model;

import java.util.*;

import be.kuleuven.cs.som.annotate.Basic;

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
		task.setUnit(null);
	}
	/**
	 * Return a copy of the set of Tasks scheduled in this Scheduler.
	 */
	@Basic
	public Set<Task> getTasks(){
		return new HashSet<Task> (this.tasks);
	}
	/**
	 * Return a boolean stating whether or not a Task is scheduled
	 * 	in this Scheduler.
	 * @param 	task
	 * 			The given Task to be checked.
	 * @return	True if and only if the given Task is in the set of
	 * 			scheduled Tasks of this Scheduler.
	 * 			| result == this.getTasks().contains(task)
	 */
	public boolean inTaskSet(Task task){
		return tasks.contains(task);
	}
	/**
	 * Return a boolean stating whether or not a given Collection of
	 * 	Tasks are scheduled in this Scheduler.
	 * @param 	c
	 * 			The given Collection of Tasks to be checked.
	 * @return	True if and only if all the elements of te given Collection
	 * 			of Tasks are in the set of scheduled Tasks of this Scheduler.
	 * 			| for each task in c : (
	 * 			|	this.getTasks().contains(c)
	 */
	public boolean inTaskSet(Collection<Task> c){
		return tasks.containsAll(c);
	}
	
	/**
	 * Return an Iterator of Tasks.
	 * @return The returned Iterator contains all the Tasks of this Scheduler
	 * 			sorted from high priority to low priority.
	 * 			| result.next().getPriority() >= result.next().getPriority()
	 * 
	 */
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
	/**
	 * Return the unassigned Task scheduled in this Scheduler with the
	 * 	highest priority of all unnasigned Tasks.
	 * @return The returned Task is unassigned and has the highest priority
	 * 			of all unassigned Tasks.
	 * 			| for each task in this.getTasks() :(
	 * 			|	result.getActor() == null && (
	 * 			|	(result.getPriority() >= task.getPriority()
	 * 			|		&& task.getActor() == null) || (task.getActor() != null)))
	 */
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
	/**
	 * Return a Comparator between Tasks. The Comparator is used to sort Tasks
	 * 	from high priority to low priority.
	 */
	private Comparator<Task> getPriorityComparator(){
		return new Comparator<Task>(){

			@Override
			public int compare(Task task1, Task task2) {
				return (int) Math.signum(task2.getPriority() - task1.getPriority());
			}
			
		};
	}

	/**
	 * The set of Tasks scheduled in this Scheduler.
	 */
	private Set<Task> tasks = new HashSet<Task>();

}
